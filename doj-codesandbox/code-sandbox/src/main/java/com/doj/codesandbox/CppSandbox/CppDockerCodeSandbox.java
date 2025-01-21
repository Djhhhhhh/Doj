package com.doj.codesandbox.CppSandbox;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.util.ArrayUtil;
import com.doj.codesandbox.CppSandbox.CppCodeSandboxTemplate;
import com.doj.codesandbox.model.ExecuteMessage;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class CppDockerCodeSandbox extends CppCodeSandboxTemplate {

    public static final Boolean FIRST_INIT = true;
    public static final long TIME_OUT = 500L;

    /**
     * 3、创建容器，把文件复制到容器内
     * @param userCodeFile
     * @param inputList
     * @return
     */
    @Override
    public List<ExecuteMessage> runFile(File userCodeFile, List<String> inputList) {
        String userCodeParentPath = userCodeFile.getParentFile().getAbsolutePath();
        // 获取默认的 Docker Client
        DockerClient dockerClient = DockerClientBuilder.getInstance().build();

        // 拉取镜像
        String image = "gcc:latest";
        if (FIRST_INIT) {
            PullImageCmd pullImageCmd = dockerClient.pullImageCmd(image);
            PullImageResultCallback pullImageResultCallback = new PullImageResultCallback() {
                @Override
                public void onNext(PullResponseItem item) {
                    System.out.println("下载镜像：" + item.getStatus());
                    super.onNext(item);
                }
            };
            try {
                pullImageCmd.exec(pullImageResultCallback).awaitCompletion();
            } catch (InterruptedException e) {
                System.out.println("拉取镜像异常");
                throw new RuntimeException(e);
            }
        }

        System.out.println("下载完成");

        // 创建容器
        CreateContainerCmd containerCmd = dockerClient.createContainerCmd(image);
        HostConfig hostConfig = new HostConfig();
        hostConfig.withMemory(100 * 1000 * 1000L);
        hostConfig.withMemorySwap(0L);
        hostConfig.withCpuCount(1L);
        hostConfig.setBinds(new Bind(userCodeParentPath, new Volume("/app")));
        CreateContainerResponse createContainerResponse = containerCmd
                .withHostConfig(hostConfig)
                .withNetworkDisabled(true)
                .withReadonlyRootfs(true)
                .withAttachStdin(true)
                .withAttachStderr(true)
                .withAttachStdout(true)
                .withTty(true)
                .exec();
        System.out.println(createContainerResponse);
        String containerId = createContainerResponse.getId();

        // 启动容器
        dockerClient.startContainerCmd(containerId).exec();

        // 编译并执行 C++ 程序
        List<ExecuteMessage> executeMessageList = new ArrayList<>();
        for (String inputArgs : inputList) {
            StopWatch stopWatch = new StopWatch();
            String[] inputArgsArray = inputArgs.split(" ");
            String[] compileCmd = new String[]{"g++", "/app/" + userCodeFile.getName(), "-o", "/app/output"};
            String[] execCmd = ArrayUtil.append(new String[]{"/app/output"}, inputArgsArray);

            // 编译 C++ 文件
            ExecCreateCmdResponse compileExecCreateCmdResponse = dockerClient.execCreateCmd(containerId)
                    .withCmd(compileCmd)
                    .withAttachStderr(true)
                    .withAttachStdin(true)
                    .withAttachStdout(true)
                    .exec();
            System.out.println("创建编译命令：" + compileExecCreateCmdResponse);

            ExecuteMessage executeMessage = new ExecuteMessage();
            final String[] message = {null};
            final String[] errorMessage = {null};
            long time = 0L;
            // 判断是否超时
            final boolean[] timeout = {true};
            String execId = compileExecCreateCmdResponse.getId();
            ExecStartResultCallback compileExecStartResultCallback = new ExecStartResultCallback() {
                @Override
                public void onComplete() {
                    // 如果编译完成，则表示没有超时
                    timeout[0] = false;
                    super.onComplete();
                }

                @Override
                public void onNext(Frame frame) {
                    StreamType streamType = frame.getStreamType();
                    if (StreamType.STDERR.equals(streamType)) {
                        errorMessage[0] = new String(frame.getPayload());
                        System.out.println("编译错误结果：" + errorMessage[0]);
                    } else {
                        message[0] = new String(frame.getPayload());
                        System.out.println("编译输出结果：" + message[0]);
                    }
                    super.onNext(frame);
                }
            };

            try {
                dockerClient.execStartCmd(execId).exec(compileExecStartResultCallback).awaitCompletion(TIME_OUT, TimeUnit.MILLISECONDS);
                if (errorMessage[0] == null) {
                    // 编译成功，继续执行程序
                    ExecCreateCmdResponse execCreateCmdResponse = dockerClient.execCreateCmd(containerId)
                            .withCmd(execCmd)
                            .withAttachStderr(true)
                            .withAttachStdin(true)
                            .withAttachStdout(true)
                            .exec();
                    System.out.println("创建执行命令：" + execCreateCmdResponse);
                    execId = execCreateCmdResponse.getId();
                    ExecStartResultCallback execStartResultCallback = new ExecStartResultCallback() {
                        @Override
                        public void onComplete() {
                            // 如果执行完成，则表示没有超时
                            timeout[0] = false;
                            super.onComplete();
                        }

                        @Override
                        public void onNext(Frame frame) {
                            StreamType streamType = frame.getStreamType();
                            if (StreamType.STDERR.equals(streamType)) {
                                errorMessage[0] = new String(frame.getPayload());
                                System.out.println("执行错误结果：" + errorMessage[0]);
                            } else {
                                message[0] = new String(frame.getPayload());
                                System.out.println("执行输出结果：" + message[0]);
                            }
                            super.onNext(frame);
                        }
                    };

                    stopWatch.start();
                    dockerClient.execStartCmd(execId).exec(execStartResultCallback).awaitCompletion(TIME_OUT, TimeUnit.MILLISECONDS);
                    stopWatch.stop();
                    time = stopWatch.getLastTaskTimeMillis();
                }
            } catch (InterruptedException e) {
                System.out.println("程序执行异常");
                throw new RuntimeException(e);
            }

            executeMessage.setMessage(message[0]);
            executeMessage.setErrorMessage(errorMessage[0]);
            executeMessage.setTime(time);
            executeMessageList.add(executeMessage);
        }
        return executeMessageList;
    }
}

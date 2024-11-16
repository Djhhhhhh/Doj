package com.doj.codesandbox.CppSandbox;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.doj.codesandbox.CodeSandbox;
import com.doj.codesandbox.model.ExecuteCodeRequest;
import com.doj.codesandbox.model.ExecuteCodeResponse;
import com.doj.codesandbox.model.ExecuteMessage;
import com.doj.codesandbox.model.JudgeInfo;
import com.doj.codesandbox.utils.ProcessUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
public class CppCodeSandboxTemplate implements CodeSandbox {

    private static final String GLOBAL_CODE_DIR_NAME = "cppTmpCode";

    private static final String GLOBAL_CPP_CLASS_NAME = "main.cpp";


    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        List<String> inputList = executeCodeRequest.getInputList();
        String code = executeCodeRequest.getCode();
        //1.把用户代码保存为文件
        File codeFile=saveCodeToFile(code);
        //2.编译代码得到class文件
        ExecuteMessage compileFileExecuteMessage=compileFile(codeFile);
        System.out.println(compileFileExecuteMessage);
        //3.执行代码，得到结果
        List<ExecuteMessage> executeMessagesList = runFile(codeFile, inputList);
        //4.收集整理输出结果
        ExecuteCodeResponse outputResponse = getOutputResponse(executeMessagesList);
        //5.文件清理
        boolean b = deleteFile(codeFile);
        if(!b){
            log.error("文件删除异常,userCodeFilePath={}",codeFile.getAbsolutePath());
        }
        return outputResponse;
    }

    /**
     * 把用户代码保存为文件
     * @param code 用户代码
     * @return 用户代码文件
     */
    public File saveCodeToFile(String code){
        String userDir = System.getProperty("user.dir");
        String globalCodePathName = userDir + File.separator + GLOBAL_CODE_DIR_NAME;
        // 判断全局代码目录是否存在，没有则新建
        if (!FileUtil.exist(globalCodePathName)) {
            FileUtil.mkdir(globalCodePathName);
        }
        // 把用户的代码隔离存放
        String userCodeParentPath = globalCodePathName + File.separator + UUID.randomUUID();
        String userCodePath = userCodeParentPath + File.separator + GLOBAL_CPP_CLASS_NAME;
        return FileUtil.writeString(code, userCodePath, StandardCharsets.UTF_8);
    }

    /**
     * 编译代码 得到class文件
     * @param file 用户代码文件
     * @return 代码编译信息
     */
    public ExecuteMessage compileFile(File file) {
       try {
           String compileCmd = String.format("g++ -finput-charset=UTF-8 -fexec-charset=UTF-8 %s -o %s", file.getAbsolutePath(), file.getAbsolutePath().substring(0, file.getAbsolutePath().length() - 4));
           Process compileProcess = Runtime.getRuntime().exec(compileCmd);
            ExecuteMessage executeMessage = ProcessUtils.runProcessAndGetMessage(compileProcess, "编译");
            if (executeMessage.getExitValue() != 0) {
                throw new RuntimeException("编译错误");
            }
            return executeMessage;
        } catch (Exception e) {
            throw new RuntimeException("编译错误", e);
        }
    }

    public List<ExecuteMessage> runFile(File file, List<String> inputList) {
        List<ExecuteMessage> executeMessageList = new ArrayList<>();
        try {
            for (String inputArgs : inputList) {
                String runCmd = String.format("cmd /c %s %s",file.getParentFile().getAbsolutePath()+File.separator+"main.exe",inputArgs);
                Process runProcess = Runtime.getRuntime().exec(runCmd);
                ExecuteMessage executeMessage = ProcessUtils.runInteractProcessAndGetMessage(runProcess,  inputArgs);
                executeMessageList.add(executeMessage);
            }
        }catch (Exception e){
            throw new RuntimeException("程序执行异常",e);
        }
        return executeMessageList;
    }

    /**
     * 收集整理输出结果
     * @param executeMessageList 结果列表
     * @return 判题结果
     */
    public ExecuteCodeResponse getOutputResponse(List<ExecuteMessage> executeMessageList){
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        List<String> outputList = new ArrayList<>();
        // 取用时最大值，便于判断是否超时
        long maxTime = 0;
        for (ExecuteMessage executeMessage : executeMessageList) {
            String errorMessage = executeMessage.getErrorMessage();
            if (StrUtil.isNotBlank(errorMessage)) {
                executeCodeResponse.setMessage(errorMessage);
                // 用户提交的代码执行中存在错误
                executeCodeResponse.setStatus(3);
                break;
            }
            outputList.add(executeMessage.getMessage());
            Long time = executeMessage.getTime();
            if (time != null) {
                maxTime = Math.max(maxTime, time);
            }
        }
        // 正常运行完成
        if (outputList.size() == executeMessageList.size()) {
            executeCodeResponse.setStatus(1);
        }
        executeCodeResponse.setOutputList(outputList);
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setTime(maxTime);
        executeCodeResponse.setJudgeInfo(judgeInfo);
        return executeCodeResponse;
    }

    /**
     * 删除文件
     * @param userCodeFile 待删除文件
     * @return 删除是否成功
     */
    public boolean deleteFile(File userCodeFile) {
        if (userCodeFile.getParentFile() != null) {
            String userCodeParentPath=userCodeFile.getParentFile().getAbsolutePath();
            boolean del = FileUtil.del(userCodeParentPath);
            System.out.println("删除" + (del ? "成功" : "失败"));
            return del;
        }
        return true;
    }

    /**
     * 获取错误响应
     *
     * @param e 错误
     * @return 错误响应
     */
    private ExecuteCodeResponse getErrorResponse(Throwable e) {
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(new ArrayList<>());
        executeCodeResponse.setMessage(e.getMessage());
        // 表示代码沙箱错误
        executeCodeResponse.setStatus(2);
        executeCodeResponse.setJudgeInfo(new JudgeInfo());
        return executeCodeResponse;
    }
}

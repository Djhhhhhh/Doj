package com.doj.dojbackendjudgeservice.judge.codesandbox;

import com.doj.dojbackendmodel.codesandbox.ExecuteCodeDTO;
import com.doj.dojbackendmodel.codesandbox.ExecuteCodeResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CodeSandboxProxy implements CodeSandbox {

    private final CodeSandbox codeSandbox;

    public CodeSandboxProxy(CodeSandbox codeSandbox) {
        this.codeSandbox = codeSandbox;
    }

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeDTO executeCodeDTO) {
        log.info("代码沙箱请求信息：" + executeCodeDTO.toString());
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeDTO);
        log.info("代码沙箱响应信息：" + executeCodeResponse.toString());
        return executeCodeResponse;
    }
}

package com.doj.dojbackendjudgeservice.judge.codesandbox.impl;

import com.doj.dojbackendjudgeservice.judge.codesandbox.CodeSandbox;
import com.doj.dojbackendmodel.codesandbox.ExecuteCodeDTO;
import com.doj.dojbackendmodel.codesandbox.ExecuteCodeResponse;

public class ThirdPartyCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeDTO executeCodeDTO){
        System.out.println("第三方代码沙箱");
        return null;
    }
}

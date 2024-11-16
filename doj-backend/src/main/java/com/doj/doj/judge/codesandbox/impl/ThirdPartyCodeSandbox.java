package com.doj.doj.judge.codesandbox.impl;

import com.doj.doj.judge.codesandbox.CodeSandbox;
import com.doj.doj.judge.codesandbox.model.ExecuteCodeDTO;
import com.doj.doj.judge.codesandbox.model.ExecuteCodeResponse;

public class ThirdPartyCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeDTO executeCodeDTO){
        System.out.println("第三方代码沙箱");
        return null;
    }
}

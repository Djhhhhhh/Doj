package com.doj.dojbackendjudgeservice.judge.codesandbox;


import com.doj.dojbackendmodel.codesandbox.ExecuteCodeDTO;
import com.doj.dojbackendmodel.codesandbox.ExecuteCodeResponse;

public interface CodeSandbox {
    ExecuteCodeResponse executeCode(ExecuteCodeDTO executeCodeDTO);
}

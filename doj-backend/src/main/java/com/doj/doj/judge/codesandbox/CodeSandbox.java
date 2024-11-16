package com.doj.doj.judge.codesandbox;

import com.doj.doj.judge.codesandbox.model.ExecuteCodeDTO;
import com.doj.doj.judge.codesandbox.model.ExecuteCodeResponse;

public interface CodeSandbox {
    ExecuteCodeResponse executeCode(ExecuteCodeDTO executeCodeDTO);
}

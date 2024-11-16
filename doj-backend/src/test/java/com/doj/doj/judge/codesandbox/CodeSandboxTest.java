package com.doj.doj.judge.codesandbox;

import com.doj.doj.judge.codesandbox.model.ExecuteCodeDTO;
import com.doj.doj.judge.codesandbox.model.ExecuteCodeResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CodeSandboxTest {

    @Value("${codesandbox.type:example}")
    private String type;
    @Test
    void executeCode() {
        CodeSandbox codeSandbox = CodeSandboxFactory.newInstance(type);
        String code ="int mian";
        String language="Java";
        List<String> inputlist= Arrays.asList("1 2","3 4");
        ExecuteCodeDTO executeCodeDTO = ExecuteCodeDTO.builder().code(code).language(language).inputList(inputlist).build();
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeDTO);
        Assertions.assertNotNull(executeCodeResponse);
    }
}
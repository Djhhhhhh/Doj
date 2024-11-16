package com.doj.codesandbox.CppSandbox;

import com.doj.codesandbox.JavaSandbox.JavaCodeSandboxTemplate;
import com.doj.codesandbox.model.ExecuteCodeRequest;
import com.doj.codesandbox.model.ExecuteCodeResponse;
import org.springframework.stereotype.Component;

/**
 * Java原生代码沙箱实现
 */
@Component
public class CppNativeCodeSandbox extends CppCodeSandboxTemplate {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        return super.executeCode(executeCodeRequest);
    }
}

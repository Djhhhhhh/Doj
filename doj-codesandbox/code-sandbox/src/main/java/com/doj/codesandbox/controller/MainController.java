package com.doj.codesandbox.controller;

import com.doj.codesandbox.CppSandbox.CppDockerCodeSandbox;
import com.doj.codesandbox.CppSandbox.CppNativeCodeSandbox;
import com.doj.codesandbox.JavaSandbox.JavaDockerCodeSandbox;
import com.doj.codesandbox.JavaSandbox.JavaNativeCodeSandbox;
import com.doj.codesandbox.model.ExecuteCodeRequest;
import com.doj.codesandbox.model.ExecuteCodeResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController("/")
public class MainController {
    private static final String AUTH_REQUEST_HEADER = "auth";
    private static final String AUTH_REQUEST_SECRET = "secreKey";
    @Resource
    private JavaNativeCodeSandbox javaNativeCodeSandbox;

    @Resource
    private JavaDockerCodeSandbox javaDockerCodeSandbox;

    @Resource
    private CppNativeCodeSandbox cppNativeCodeSandbox;

    @Resource
    private CppDockerCodeSandbox cppDockerCodeSandbox;


    @Value("${environment.type:docker}")
    private String enviromentType;
    /**
     * 执行代码
     * @param executeCodeRequest
     * @return
     */
    @PostMapping("/executeCode")
    ExecuteCodeResponse executeCode(@RequestBody ExecuteCodeRequest executeCodeRequest, HttpServletRequest request, HttpServletResponse response){
        String authHeader= request.getHeader(AUTH_REQUEST_HEADER);
        if(!AUTH_REQUEST_SECRET.equals(authHeader)){
            response.setStatus(403);
            return null;
        }
        if (executeCodeRequest==null){
            throw new RuntimeException("请求参数异常");
        }
        if(enviromentType.equals("docker")){
            if (executeCodeRequest.getLanguage().equals("cpp")) {
                return cppDockerCodeSandbox.executeCode(executeCodeRequest);
            }else{
                return javaDockerCodeSandbox.executeCode(executeCodeRequest);
            }
        }else{
            if (executeCodeRequest.getLanguage().equals("cpp")) {
                return cppNativeCodeSandbox.executeCode(executeCodeRequest);
            }else{
                return javaNativeCodeSandbox.executeCode(executeCodeRequest);
            }
        }
    }
}


package com.doj.dojbackendjudgeservice.judge.codesandbox.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.doj.dojbackendcommon.common.ErrorCode;
import com.doj.dojbackendcommon.exception.BusinessException;
import com.doj.dojbackendjudgeservice.judge.codesandbox.CodeSandbox;
import com.doj.dojbackendmodel.codesandbox.ExecuteCodeDTO;
import com.doj.dojbackendmodel.codesandbox.ExecuteCodeResponse;
import org.apache.commons.lang3.StringUtils;

public class RemoteCodeSandbox implements CodeSandbox {
    private static final String AUTH_REQUEST_HEADER = "auth";
    private static final String AUTH_REQUEST_SECRET = "secreKey";
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeDTO executeCodeDTO){
        System.out.println("远程代码沙箱");
        String url="http://localhost:8090/executeCode";
        String json= JSONUtil.toJsonStr(executeCodeDTO);
        String responseStr = HttpUtil.createPost(url).header(AUTH_REQUEST_HEADER,AUTH_REQUEST_SECRET).body(json).execute().body();
        if(StringUtils.isBlank(responseStr)){
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR,"executeCode remoteSandbox error, message= "+responseStr);
        }
        System.out.println(responseStr);
        return JSONUtil.toBean(responseStr,ExecuteCodeResponse.class);
    }
}

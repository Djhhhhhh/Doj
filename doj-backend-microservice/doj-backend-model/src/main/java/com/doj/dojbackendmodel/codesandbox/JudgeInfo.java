package com.doj.dojbackendmodel.codesandbox;

import lombok.Data;

/**
 * 题目信息
 */
@Data
public class JudgeInfo {
    private String message;
    private long time;
    private long memory;
}


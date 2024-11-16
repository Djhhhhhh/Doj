package com.doj.dojbackendmodel.dto.question;

import lombok.Data;

/**
 * 题目配置
 */
@Data
public class JudgeConfig {
    private long timeLimit;
    private long memoryLimit;
    private long stackLimit;
}


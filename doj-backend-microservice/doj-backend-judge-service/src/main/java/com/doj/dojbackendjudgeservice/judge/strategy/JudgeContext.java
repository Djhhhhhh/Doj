package com.doj.dojbackendjudgeservice.judge.strategy;

import com.doj.dojbackendmodel.codesandbox.JudgeInfo;
import com.doj.dojbackendmodel.dto.question.JudgeCase;
import com.doj.dojbackendmodel.entity.Question;
import com.doj.dojbackendmodel.entity.QuestionSubmit;
import lombok.Data;

import java.util.List;

/**
 * 上下文（用于定义在策略中传递的参数）
 */
@Data
public class JudgeContext {

    private JudgeInfo judgeInfo;

    private List<String> inputList;

    private List<String> outputList;

    private List<JudgeCase> judgeCaseList;

    private Question question;

    private QuestionSubmit questionSubmit;

}

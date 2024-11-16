package com.doj.doj.judge;

import com.doj.doj.judge.strategy.DefaultJudgeStrategy;
import com.doj.doj.judge.strategy.JavaLanguageJudgeStrategy;
import com.doj.doj.judge.strategy.JudgeContext;
import com.doj.doj.judge.strategy.JudgeStrategy;
import com.doj.doj.model.dto.questionsubmit.JudgeInfo;
import com.doj.doj.model.entity.QuestionSubmit;
import org.springframework.stereotype.Service;

@Service
public class JudgeManager {

    /**
     * 执行判题
     *
     * @param judgeContext
     * @return
     */
    JudgeInfo doJudge(JudgeContext judgeContext) {
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        if ("java".equals(language)) {
            judgeStrategy = new JavaLanguageJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }

}
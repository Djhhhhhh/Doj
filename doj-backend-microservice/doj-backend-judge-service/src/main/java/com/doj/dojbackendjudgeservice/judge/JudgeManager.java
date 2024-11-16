package com.doj.dojbackendjudgeservice.judge;

import com.doj.dojbackendjudgeservice.judge.strategy.DefaultJudgeStrategy;
import com.doj.dojbackendjudgeservice.judge.strategy.JavaLanguageJudgeStrategy;
import com.doj.dojbackendjudgeservice.judge.strategy.JudgeContext;
import com.doj.dojbackendjudgeservice.judge.strategy.JudgeStrategy;
import com.doj.dojbackendmodel.codesandbox.JudgeInfo;
import com.doj.dojbackendmodel.entity.QuestionSubmit;
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
        JudgeStrategy judgeStrategy;
        if ("java".equals(language)) {
            judgeStrategy = new JavaLanguageJudgeStrategy();
        }else{
            judgeStrategy = new DefaultJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }

}
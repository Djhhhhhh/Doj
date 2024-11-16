package com.doj.dojbackendquestionservice.controller.inner;

import com.doj.dojbackendmodel.entity.Question;
import com.doj.dojbackendmodel.entity.QuestionSubmit;
import com.doj.dojbackendquestionservice.service.QuestionService;
import com.doj.dojbackendquestionservice.service.QuestionSubmitService;
import com.doj.dojbackendserviceclient.service.QuestionFeignClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/inner")
public class QuestionInnerController implements QuestionFeignClient {

    @Resource
    private QuestionService questionService;
    @Resource
    private QuestionSubmitService questionSubmitService;

    @Override
    @GetMapping("/get/id")
    public Question getQuestionById(@RequestParam("questionId") long questionId){
        return questionService.getById(questionId);
    }

    @Override
    @GetMapping("/question_submit/get/id")
    public QuestionSubmit getQuestionSubmitById(@RequestParam("questionSubmitId") long questionSubmitId){
        return questionSubmitService.getById(questionSubmitId);
    }

    @Override
    @PostMapping("/question_submit/update")
    public boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmit){
        return questionSubmitService.updateById(questionSubmit);
    }

    @Override
    @PostMapping("/question_submit/update/num")
    public void addSubmitOrAcById(@RequestParam("questionId") long questionId,@RequestParam("type") String type) {
        Question question=new Question();
        question=questionService.getById(questionId);
        if("Sub".equals(type))
            question.setSubmitNum(question.getSubmitNum()+1);
        else
            question.setAcNum(question.getAcNum()+1);
        questionService.updateById(question);
    }
}

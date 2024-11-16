package com.doj.dojbackendserviceclient.service;


import com.doj.dojbackendmodel.entity.QuestionSubmit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "doj-backend-judge-service",path = "/api/judge/inner")
public interface JudgeFeignClient {


    @PostMapping("/do")
    QuestionSubmit doJudge(@RequestParam("questionSubmitId") long questionSubmitId);
}

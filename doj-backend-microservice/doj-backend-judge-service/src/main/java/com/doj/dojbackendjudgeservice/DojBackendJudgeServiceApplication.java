package com.doj.dojbackendjudgeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan("com.doj")
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true,exposeProxy = true)
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.doj.dojbackendserviceclient.service"})
public class DojBackendJudgeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DojBackendJudgeServiceApplication.class, args);
    }

}

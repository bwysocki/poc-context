package com.example.demo.controller;

import com.example.demo.annotation.Context;
import com.example.demo.dto.context.DemoContext;
import com.example.demo.dto.rest.DemoA;
import com.example.demo.dto.rest.DemoB;
import com.example.demo.service.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class DemoController {

    @Autowired
    private DemoContext demoContext; // demo context is bean with request scope

    @Autowired
    private DemoService demoService;

    @Context
    @PostMapping("/demo_a")
    public String demoA(@RequestBody DemoA demoA) {
        log.info("------------------[Controller: demoA, trace id = " + demoContext.getTraceId() + "]-------------------");
        return demoService.demo();
    }

    @Context
    @PostMapping("/demo_b")
    public String demoB(@RequestBody DemoB demoB) {
        log.info("------------------[Controller: demoB, trace id = " + demoContext.getTraceId() + "]-------------------");
        return demoService.demo();
    }

}

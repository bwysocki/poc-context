package com.example.demo.service;

import com.example.demo.dto.context.DemoContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service // this is singleton bean
@Slf4j
public class DemoService {

    @Autowired
    private DemoContext demoContext; // this is request bean


    public String demo() {
        log.info("-------- singleton bean = " + this + ", request bean = " + demoContext);
        return "Received context: " + demoContext.getMessage();
    }
}

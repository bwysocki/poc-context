package com.example.demo.controller;

import com.example.demo.dto.rest.DemoA;
import com.example.demo.dto.rest.DemoB;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DemoControllerITest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void testDemoA() throws Exception {
        DemoA demoA = DemoA.builder().demo1("demo 1").demo2("demo 2").build();

        mockMvc.perform(post("/demo_a")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(demoA)))
                .andExpect(status().isOk())
                .andExpect(content().string("Received context: demo 1_demo 2"));
    }

    @Test
    public void testDemoB() throws Exception {
        DemoB demoB = DemoB.builder().demo1("demo 1").build();

        mockMvc.perform(post("/demo_b")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(demoB)))
                .andExpect(status().isOk())
                .andExpect(content().string("Received context: demo 1"));
    }

    @Test
    public void testConcurrentSpringBeans() throws Exception {
        //just run 10 concurrent requests
        List<CompletableFuture<Void>> requests = new ArrayList<>();
        for (int i = 0; i < 10; i ++) {
            requests.add(makeRequest());
        }

        CompletableFuture.allOf(requests.toArray(new CompletableFuture[0])).join();

        //after finishing - you can see in logs that:
        //- singleton bean (DemoService) is always the same
        //- request bean is new per request
    }

    private CompletableFuture<Void> makeRequest() {
        return CompletableFuture.runAsync(() -> {

            DemoA demoA = DemoA.builder()
                    .demo1(UUID.randomUUID().toString())
                    .demo2(UUID.randomUUID().toString())
                    .build();

            try {
                mockMvc.perform(post("/demo_a")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(demoA)))
                        .andExpect(status().isOk())
                        .andExpect(content().string("Received context: " + demoA.getDemo1() + "_" + demoA.getDemo2()));
            } catch (Exception e) {
                throw new RuntimeException("Shoud not happen");
            }
        });
    }

}

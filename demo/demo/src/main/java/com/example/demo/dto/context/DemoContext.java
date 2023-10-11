package com.example.demo.dto.context;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class DemoContext {

    private UUID traceId;
    private String message;

}

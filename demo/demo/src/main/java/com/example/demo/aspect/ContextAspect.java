package com.example.demo.aspect;

import com.example.demo.dto.context.DemoContext;
import com.example.demo.dto.rest.DemoA;
import com.example.demo.dto.rest.DemoB;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Aspect
public class ContextAspect {

    @Autowired
    private DemoContext demoContext;

    @Around("@annotation(com.example.demo.annotation.Context)")
    public Object generateContext(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();

        // we can initialize DemoContext here, or we can create more methods like generateContext()
        if (args.length > 0) {
            Object arg = args[0];

            demoContext.setTraceId(UUID.randomUUID());
            if (arg instanceof DemoA demoA) {
                demoContext.setMessage(demoA.getDemo1() + "_" + demoA.getDemo2());
            } else if (arg instanceof DemoB demoB) {
                demoContext.setMessage(demoB.getDemo1());
            }
        }

        return joinPoint.proceed();
    }

}

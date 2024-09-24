package com.example.realworld.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;

import java.util.stream.Collectors;

@Aspect
@Component
@Slf4j
public class ValidationAspect {

    //    @Pointcut("execution(* com.example.realworld.domain..*.*(.., BindingResult))")
//    @Pointcut("execution(* com.example.realworld.domain.user.controller..*.*(.., BindingResult))")
    @Pointcut("@annotation( com.example.realworld.common.aop.CheckBindingErrors)")
    public void bindingResultPointcut() {
    }

    @Before("bindingResultPointcut() && args(.., bindingResult)")
//    @Before("execution(* com.example.realworld.domain..*.*(.., BindingResult))")
    public void validateBindingResult(BindingResult bindingResult) throws BindException {

        log.info("validationAop is called");

        if (bindingResult.hasErrors()) {

            String errorMessage = bindingResult
                    .getAllErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            log.info("Validation errors; {} ", errorMessage);

            throw new BindException(bindingResult);
        }
    }
}

/*
 * 
 */
package com.example.hello.impl;

import akka.NotUsed;
import com.example.hello.api.HelloService;
import com.lightbend.lagom.javadsl.api.ServiceCall;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * Implementation of the HelloService.
 */
public class HelloServiceImpl implements HelloService {

    @Inject
    public HelloServiceImpl() {
    }

    @Override
    public ServiceCall<NotUsed, String> factorial(String id) {
        return request -> {
//            System.out.println("Received request for " +id);
            int failureId = 11;
            int failure = IntStream.range(1, failureId).reduce(1, (a, b) -> a * b);
            int factorial = IntStream.range(1, Integer.valueOf(id)).reduce(1, (a, b) -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return a * b;
            });

//            if (factorial == failure)
//                throw new RuntimeException(failureId + "! is too much");

            return CompletableFuture.completedFuture(Integer.toString(factorial));
        };
    }

}


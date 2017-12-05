/*
 * 
 */
package com.example.hellostream.impl;

import akka.NotUsed;
import akka.stream.Materializer;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import com.example.hello.api.HelloService;
import com.example.hellostream.api.HelloStreamService;
import com.lightbend.lagom.javadsl.api.ServiceCall;

import javax.inject.Inject;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Implementation of the HelloStreamService.
 */
public class HelloStreamServiceImpl implements HelloStreamService {

    private final HelloService helloService;
    private Materializer materializer;

    @Inject
    public HelloStreamServiceImpl(HelloService helloService, Materializer materializer) {
        this.helloService = helloService;
        this.materializer = materializer;
    }


    @Override
    public ServiceCall<NotUsed, String> launch() {
        return (NotUsed req) -> {
            System.out.println("\nReceived request  !!");

            char c =
                    new char[]{'.','x','-',':','*'}[new Random().nextInt(5)];

            Stream<Integer> boxed = IntStream
                    .range(1, 100)
                    .boxed();

//            Stream<String> completionStageStream =
            boxed.map(Object::toString)
                    .map(str -> helloService
                                    .factorial(str)
                                    .invoke()
                                    .toCompletableFuture()
                                    .thenApply(s -> {
//                                System.out.println("finished: " + str);
                                        return s;
                                    })
                                    .exceptionally(Throwable::getLocalizedMessage)
                    )
                    .map(CompletableFuture::join)
                    .forEach(x -> System.out.print(""+c))
            ;

            return CompletableFuture.completedFuture("Done");

        };
    }

    @Override
    public ServiceCall<NotUsed, String> launchBackPressured() {
        return (NotUsed req) -> {

            List<Integer> integers = IntStream
                    .range(1, 100)
                    .boxed()
                    .collect(Collectors.toList());

            Source<Integer, NotUsed> source = Source.from(integers);

            Sink<String, CompletionStage<List<String>>> sink = Sink.seq();
            CompletionStage<List<String>> completionStage = source
                    .map(Object::toString)
                    .mapAsync(4,
                            str -> helloService
                                    .factorial(str)
                                    .invoke()
                                    .toCompletableFuture()
                                    .thenApply(x -> {
                                        System.out.println("finished " + str);
                                        return x;
                                    })
                    )
                    .runWith(sink, materializer);

            completionStage.toCompletableFuture().join();


            return CompletableFuture.completedFuture("Done");

        };
    }
}

/*
 * 
 */
package com.example.hello.api;

import static com.lightbend.lagom.javadsl.api.Service.named;
import static com.lightbend.lagom.javadsl.api.Service.pathCall;
import static com.lightbend.lagom.javadsl.api.Service.topic;

import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;

/**
 * The factorial service interface.
 * <p>
 * This describes everything that Lagom needs to know about how to serve and
 * consume the HelloService.
 */
public interface HelloService extends Service {

  /**
   * Example: curl http://localhost:9000/api/factorial/Alice
   */
  ServiceCall<NotUsed, String> factorial(String id);



  @Override
  default Descriptor descriptor() {
    // @formatter:off
    return named("factorial").withCalls(
        pathCall("/api/factorial/:id",  this::factorial)
      ).withAutoAcl(true);
    // @formatter:on
  }
}

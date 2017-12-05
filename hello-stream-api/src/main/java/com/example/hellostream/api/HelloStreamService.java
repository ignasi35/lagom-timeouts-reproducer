/*
 * 
 */
package com.example.hellostream.api;

import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.transport.Method;

import static com.lightbend.lagom.javadsl.api.Service.named;
import static com.lightbend.lagom.javadsl.api.Service.restCall;


public interface HelloStreamService extends Service {

  ServiceCall<NotUsed, String> launch();
  ServiceCall<NotUsed, String> launchBackPressured();

  @Override
  default Descriptor descriptor() {
    return named("hello-stream")
        .withCalls(
            restCall(Method.GET, "/api/launch", this::launch),
            restCall(Method.GET, "/api/launchBackPressured", this::launchBackPressured)
        ).withAutoAcl(true);
  }
}

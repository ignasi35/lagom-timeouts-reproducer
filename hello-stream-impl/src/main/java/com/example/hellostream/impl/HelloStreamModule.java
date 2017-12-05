/*
 * 
 */
package com.example.hellostream.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import com.example.hello.api.HelloService;
import com.example.hellostream.api.HelloStreamService;

/**
 * The module that binds the HelloStreamService so that it can be served.
 */
public class HelloStreamModule extends AbstractModule implements ServiceGuiceSupport {
  @Override
  protected void configure() {
    bindService(HelloStreamService.class, HelloStreamServiceImpl.class);
    bindClient(HelloService.class);
  }
}

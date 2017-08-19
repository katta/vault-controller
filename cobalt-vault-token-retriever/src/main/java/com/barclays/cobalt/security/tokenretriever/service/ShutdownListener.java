package com.barclays.cobalt.security.tokenretriever.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ExitCodeEvent;
import org.springframework.context.ApplicationListener;

public class ShutdownListener implements ApplicationListener<ExitCodeEvent> {

  private static final Logger logger = LoggerFactory.getLogger(ShutdownListener.class);
  @Override
  public void onApplicationEvent(ExitCodeEvent event) {
    logger.info("Exiting JVM with exit code: {}", event.getExitCode());
    System.exit(event.getExitCode());
  }
}

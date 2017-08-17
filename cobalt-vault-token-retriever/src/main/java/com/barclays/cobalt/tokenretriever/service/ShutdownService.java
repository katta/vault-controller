package com.barclays.cobalt.tokenretriever.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import java.util.Timer;
import java.util.TimerTask;


public class ShutdownService {
  private static final Logger logger = LoggerFactory.getLogger(ShutdownService.class);
  private final ApplicationContext context;
  private final int timeoutDelayInSeconds;

  public ShutdownService(ApplicationContext context, int timeoutDelayInSeconds) {
    this.context = context;
    this.timeoutDelayInSeconds = timeoutDelayInSeconds;
  }

  public void abnormally() {
    exit(-1);
  }

  public void normally() {
    exit(0);
  }

  private void exit(int code) {
    String message = code == 0 ? "normal" : "abnormal";
    logger.info("Initiating {} shutdown with exit code: {}.", message, code);
    SpringApplication.exit(context, () -> code);
    logger.info("Application context is closed. Exiting...");
    System.exit(code);
  }

  public void initiateDelayedShutdown() {
    initiate(-1);
  }

  private void initiate(int exitCode) {
    logger.info("Scheduling shutdown with exit code {}. Application will exit in {} seconds.", exitCode, timeoutDelayInSeconds);
    new Timer("shutdown-timer").schedule(new TimerTask() {
      @Override
      public void run() {
        exit(exitCode);
      }
    }, timeoutDelayInSeconds * 1000);
  }
}

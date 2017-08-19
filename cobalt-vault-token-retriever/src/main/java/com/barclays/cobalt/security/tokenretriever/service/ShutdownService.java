package com.barclays.cobalt.security.tokenretriever.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ExitCodeEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Timer;
import java.util.TimerTask;


public class ShutdownService {
  private static final Logger logger = LoggerFactory.getLogger(ShutdownService.class);
  private final ApplicationContext context;
  private final int timeoutDelayInSeconds;
  private final Timer timer;

  public ShutdownService(ApplicationContext context, int timeoutDelayInSeconds, Timer timer) {
    this.context = context;
    this.timeoutDelayInSeconds = timeoutDelayInSeconds;
    this.timer = timer;
  }

  public void abnormally() {
    exit(-1);
  }

  public void normally() {
    exit(0);
  }

  public void delayed() {
    initiate(-1);
  }

  private void exit(int exitCode) {
    String message = exitCode == 0 ? "normal" : "abnormal";
    logger.info("Initiating {} shutdown with exit code: {}.", message, exitCode);
    context.publishEvent(new ExitCodeEvent(context, exitCode));
    if (context instanceof ConfigurableApplicationContext) {
      ConfigurableApplicationContext closable = (ConfigurableApplicationContext) context;
      closable.close();
    }
    logger.info("Application context is now closed.");
  }

  private void initiate(int exitCode) {
    logger.info("Scheduling shutdown with exit code {}. Application will exit in {} seconds.", exitCode, timeoutDelayInSeconds);
    timer.schedule(new ShutdownTask(exitCode), timeoutDelayInSeconds * 1000);
  }

  class ShutdownTask extends TimerTask {
    private final int exitCode;

    ShutdownTask(int exitCode) {
      this.exitCode = exitCode;
    }

    @Override
    public void run() {
      exit(exitCode);
    }
  }
}

package com.barclays.cobalt.vaultcontroller;

import com.barclays.cobalt.vaultcontroller.config.ApplicationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties.class)
@EnableAsync
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}

package com.barclays.cobalt.vault.vaultcontroller;

import com.barclays.cobalt.vault.vaultcontroller.config.OpenshiftProperties;
import com.barclays.cobalt.vault.vaultcontroller.config.VaultProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableConfigurationProperties(value = {OpenshiftProperties.class, VaultProperties.class})
@EnableAsync
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}

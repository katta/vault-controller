package com.barclays.cobalt.security.initializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.vault.config.VaultBootstrapConfiguration;
import org.springframework.cloud.vault.config.VaultProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Configuration
@EnableConfigurationProperties({VaultProperties.class})
@AutoConfigureBefore({VaultBootstrapConfiguration.class})
@Lazy(false)
public class VaultInitConfiguration implements InitializingBean {

  private static final Logger logger = LoggerFactory.getLogger(VaultInitConfiguration.class);

  @Value("${vault.token.file:/var/run/secrets/vaultproject.io/token}")
  private String secretFile;

  private VaultProperties vaultProperties;

  public VaultInitConfiguration(VaultProperties vaultProperties) {
    this.vaultProperties = vaultProperties;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    logger.debug("Vault token is being set to vault properties in afterPropertiesSet ");
    vaultProperties.setToken(vaultToken());
  }

  private String vaultToken() throws IOException {
    String token;
    try {
      logger.debug("Vault token file is at '{}", secretFile);
      token = new String(Files.readAllBytes(Paths.get(secretFile)));
    } finally {
      logger.info("Attempting to delete the vault token file '{}' after initializing vault properties", secretFile);
      boolean isDeleted = Files.deleteIfExists(Paths.get(secretFile));
      logger.info("Vault token file deletion {}", isDeleted ? "completed" : "failed");
    }
    return token;
  }
}

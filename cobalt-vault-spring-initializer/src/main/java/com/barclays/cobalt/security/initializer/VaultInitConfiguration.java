package com.barclays.cobalt.security.initializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.vault.config.VaultBootstrapConfiguration;
import org.springframework.cloud.vault.config.VaultProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

@Configuration
@EnableConfigurationProperties({VaultProperties.class})
@AutoConfigureBefore(VaultBootstrapConfiguration.class)
@Order(HIGHEST_PRECEDENCE)
@Lazy(false)
@ConditionalOnProperty(value = "spring.cloud.vault.enabled", havingValue = "true")
public class VaultInitConfiguration implements InitializingBean {

  private static final Logger logger = LoggerFactory.getLogger(VaultInitConfiguration.class);

  @Value("${vault.token.file:file:///var/run/secrets/vaultproject.io/token}")
  private Resource secretFile;

  private VaultProperties vaultProperties;

  public VaultInitConfiguration(VaultProperties vaultProperties) {
    this.vaultProperties = vaultProperties;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    String token = readTokenFromFile();
    logger.info("Read vault token with value '{}' from path '{}'", token, secretFile);
    vaultProperties.setToken(token);
  }

  private String readTokenFromFile() throws IOException {
    logger.info("Reading vault token file at '{}", secretFile);
    Path tokenFile = Paths.get(secretFile.getURI());
    if (!Files.isReadable(tokenFile)) {
      logger.error("Unable to read token file from location '{}'", tokenFile);
      throw new IllegalStateException("Unable to read token file at location: " + secretFile);
    }
    return new String(Files.readAllBytes(tokenFile)).replaceAll("(?:[\\n\\r])", "");
  }
}

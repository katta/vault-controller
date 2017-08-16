package com.barclays.cobalt.security.config;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.nio.file.Path;
import java.nio.file.Paths;

@ConfigurationProperties(prefix = "token-retriever")
@Data
@Validated
public class ApplicationProperties {
  @NotBlank
  private String vaultControllerBaseUri;
  @NotBlank
  private String podNamespace;
  @NotBlank
  private String podName;
  @NotBlank
  private String tokenPath;

  public Path tokenPath() {
    return Paths.get(tokenPath);
  }

}

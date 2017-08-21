package com.barclays.cobalt.security.tokenretriever.config;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@ConfigurationProperties(prefix = "application")
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
  private String tokenFilePath;

  @Valid
  private VaultProperties vault;
  @Min(10)
  private int callbackTimeout = 60;


  public Path tokenFilePath() {
    return Paths.get(tokenFilePath);
  }

  @Data
  public static class VaultProperties {
    @NotNull
    private URI baseUri;
    private String version = "v1";


    public URI unwrapEndpoint() {
      return UriComponentsBuilder.newInstance()
          .uri(baseUri)
          .path(version)
          .path("/sys/wrapping/unwrap")
          .build().toUri();
    }
  }
}

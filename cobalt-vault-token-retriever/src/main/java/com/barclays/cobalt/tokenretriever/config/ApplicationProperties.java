package com.barclays.cobalt.tokenretriever.config;

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

@ConfigurationProperties(prefix = "tokenretriever")
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

  @Valid
  private VaultProperties vault;
  @Min(10)
  private int callbackTimeout = 10;


  public Path tokenPath() {
    return Paths.get(tokenPath);
  }

  @Data
  public static class VaultProperties {
    @NotNull
    private String host;
    @NotNull
    private Integer port;
    private String scheme = "https";
    private String version = "v1";


    public URI unwrapEndpoint() {
      return UriComponentsBuilder.newInstance()
          .host(host)
          .port(port)
          .scheme(scheme)
          .path(version)
          .path("/sys/wrapping/unwrap")
          .build().toUri();
    }
  }
}

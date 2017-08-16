package com.barclays.cobalt.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.constraints.NotNull;
import java.net.URI;

@ConfigurationProperties(prefix = "token-retriever.vault")
@Data
@Validated
public class VaultProperties {
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


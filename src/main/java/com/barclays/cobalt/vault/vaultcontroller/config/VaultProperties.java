package com.barclays.cobalt.vault.vaultcontroller.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.constraints.NotNull;

@ConfigurationProperties(prefix = "vault")
@Data
public class VaultProperties {
  @NotNull
  private String host;
  @NotNull
  private Integer port;
  private String scheme = "https";
  private String version = "v1";
  @NotNull
    private String rootToken;
  @NotNull
  private Long wrapTtlInSeconds;


  public String tokenEndpoint() {
    return UriComponentsBuilder.newInstance()
        .host(host)
        .port(port)
        .scheme(scheme)
        .path(version)
        .path("/auth/token/create")
        .build().toUriString();
  }

  public String getWrapTtl() {
    return wrapTtlInSeconds + "s";
  }
}

package com.barclays.cobalt.vault.vaultcontroller.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.UriComponentsBuilder;

@Configuration
public class VaultConfiguration {
  @Value("${vault.host}")
  private String vaultHost;
  @Value("${vault.port}")
  private Integer vaultPort;
  @Value("${vault.http.scheme:http}")
  private String vaultHttpScheme;


  public String vaultAPIEndpoint() {
    return UriComponentsBuilder.newInstance()
        .host(vaultHost)
        .port(vaultPort)
        .scheme(vaultHttpScheme)
        .path("v1")
        .build().toUriString();
  }
}

package com.barclays.cobalt.vault.vaultcontroller.config;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.constraints.NotNull;

@ConfigurationProperties(prefix = "openshift")
@Data
public class OpenshiftProperties {
  @NotBlank
  private String host;
  @NotNull
  private Integer port;
  @NotBlank
  private String authToken;
  private String scheme = "https";
  private boolean verifySslHostName = false;

  public String baseUrl() {
    return UriComponentsBuilder.newInstance()
        .host(host)
        .port(port)
        .scheme(scheme)
        .build().toUriString();
  }


}

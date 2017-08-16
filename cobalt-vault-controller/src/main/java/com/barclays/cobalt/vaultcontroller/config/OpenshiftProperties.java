package com.barclays.cobalt.vaultcontroller.config;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.constraints.NotNull;

@ConfigurationProperties(prefix = "openshift")
@Data
@Validated
public class OpenshiftProperties {
  @NotBlank
  private String host;
  @NotNull
  @Range(min = 1, max = 65535)
  private Integer port;
  @NotBlank
  private String authToken;
  private String scheme = "https";
  private boolean verifySslHostName = false;
  @NotNull
  @Range(min = 1, max = 65535)
  private Integer initContainerPort;

  public String baseUrl() {
    return UriComponentsBuilder.newInstance()
        .host(host)
        .port(port)
        .scheme(scheme)
        .build().toUriString();
  }
}

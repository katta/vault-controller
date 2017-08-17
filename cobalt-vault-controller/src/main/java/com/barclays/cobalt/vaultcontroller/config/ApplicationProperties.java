package com.barclays.cobalt.vaultcontroller.config;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@ConfigurationProperties(prefix = "vaultcontroller")
@Data
@Validated
public class ApplicationProperties {
  @Valid
  private OpenshiftProperties openshift;

  @Valid
  private VaultProperties vault;


  @Data
  public static class VaultProperties {
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

  @Data
  public static class OpenshiftProperties {
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
}

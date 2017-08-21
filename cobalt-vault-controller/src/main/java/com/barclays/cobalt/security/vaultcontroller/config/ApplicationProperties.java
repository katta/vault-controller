package com.barclays.cobalt.security.vaultcontroller.config;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;

@ConfigurationProperties(prefix = "application")
@Data
@Validated
public class ApplicationProperties {
  @Valid
  private OpenshiftProperties openshift;

  @Valid
  private VaultProperties vault;

  @NotNull
  @Range(min = 1, max = 65535)
  private Integer initContainerPort;

  @Data
  public static class VaultProperties {
    @NotNull
    private URI baseUri;
    private String version = "v1";
    @NotBlank
    private String rootToken;
    @NotNull
    private Long wrapTtlInSeconds;


    public String tokenEndpoint() {
      return UriComponentsBuilder.newInstance()
          .uri(baseUri)
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
    @NotNull
    private URI baseUri;
    @NotBlank
    private String authToken;
    private boolean verifySslHostName = false;

    public String baseUri() {
      return UriComponentsBuilder.newInstance()
          .uri(baseUri)
          .build().toUriString();
    }
  }
}

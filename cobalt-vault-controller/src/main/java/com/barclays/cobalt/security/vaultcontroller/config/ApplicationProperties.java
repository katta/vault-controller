package com.barclays.cobalt.security.vaultcontroller.config;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

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
    public static final String DEFAULT_AUTH_TOKEN_FILE = "/var/run/secrets/kubernetes.io/serviceaccount/token";
    @NotNull
    private URI baseUri;
    @NotNull
    private Resource authTokenFile = new FileSystemResource(DEFAULT_AUTH_TOKEN_FILE);
    private boolean verifySslHostName = false;

    public String authToken() {
      try {
        return new String(Files.readAllBytes(Paths.get(authTokenFile.getURI())));
      } catch (IOException e) {
        throw new IllegalStateException("Failed to read auth token from location:" + authTokenFile, e);
      }
    }

    public String baseUri() {
      return UriComponentsBuilder.newInstance()
          .uri(baseUri)
          .build().toUriString();
    }
  }
}

package com.barclays.cobalt.vault.vaultcontroller.domain;

import com.barclays.cobalt.vault.vaultcontroller.config.VaultConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

public class TokenGenerator {

  private final RestTemplate http;
  private final VaultConfiguration configuration;

  public TokenGenerator(RestTemplateBuilder builder, VaultConfiguration configuration) {
    this.http = builder
        .additionalInterceptors(new VaultHttpRequestInterceptor(configuration)).build();
    this.configuration = configuration;
  }

  public VaultWrappedResponse generateToken(String namespace, String podName, String... policies) {

    VaultTokenRequest request = VaultTokenRequest.builder()
        .withPolicies(policies)
        .withMeta("namespace", namespace)
        .displayName(podName)
        .ttl(configuration.getWrapTtlInSeconds())
        .build();

    return http.postForObject(configuration.tokenEndpoint(), request, VaultWrappedResponse.class);
  }

}

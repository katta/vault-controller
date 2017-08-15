package com.barclays.cobalt.vault.vaultcontroller.domain;

import com.barclays.cobalt.vault.vaultcontroller.config.VaultProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TokenGenerator {

  private final RestTemplate http;
  private final VaultProperties configuration;

  public TokenGenerator(RestTemplateBuilder builder, VaultProperties configuration) {
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

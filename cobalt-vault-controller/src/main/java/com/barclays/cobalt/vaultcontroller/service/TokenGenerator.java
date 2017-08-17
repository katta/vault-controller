package com.barclays.cobalt.vaultcontroller.service;

import com.barclays.cobalt.vaultcontroller.config.ApplicationProperties.VaultProperties;
import com.barclays.cobalt.vaultcontroller.domain.VaultTokenRequest;
import com.barclays.cobalt.vaultcontroller.domain.VaultWrappedResponse;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import java.util.Set;

public class TokenGenerator {

  private final RestTemplate http;
  private final VaultProperties configuration;

  public TokenGenerator(RestTemplateBuilder builder, VaultProperties configuration) {
    this.http = builder
        .additionalInterceptors(new VaultHttpRequestInterceptor(configuration)).build();
    this.configuration = configuration;
  }

  public VaultWrappedResponse generateToken(String namespace, String podName, Set<String> policies) {
    return generateToken(namespace, podName, policies.toArray(new String[policies.size()]));
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

package com.barclays.cobalt.security.vaultcontroller.service;

import com.barclays.cobalt.security.vaultcontroller.config.ApplicationProperties.VaultProperties;
import com.barclays.cobalt.security.vaultcontroller.domain.VaultTokenRequest;
import com.barclays.cobalt.security.vaultcontroller.domain.VaultWrappedResponse;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.util.Set;

public class TokenGenerator {

  static final String TOKEN_HEADER = "X-Vault-Token";
  static final String WRAP_RESPONSE_TTL_HEADER = "X-Vault-Wrap-TTL";
  private final RestTemplate http;
  private final VaultProperties configuration;

  public TokenGenerator(RestTemplateBuilder builder, VaultProperties configuration) {
    this.http = builder.build();
    this.configuration = configuration;
  }

  public VaultWrappedResponse generateToken(String namespace, String podName, Set<String> policies) {
    return generateToken(namespace, podName, policies.toArray(new String[policies.size()]));
  }

  public VaultWrappedResponse generateToken(String namespace, String podName, String... policies) {

    HttpHeaders headers = new HttpHeaders();
    headers.add(TOKEN_HEADER, configuration.getRootToken());
    headers.add(WRAP_RESPONSE_TTL_HEADER, configuration.getWrapTtl());
    HttpEntity<VaultTokenRequest> request = new HttpEntity<>(VaultTokenRequest.builder()
        .withPolicies(policies)
        .withMeta("namespace", namespace)
        .displayName(podName)
        .ttl(configuration.getWrapTtlInSeconds())
        .build(), headers);
    return http.postForObject(configuration.tokenEndpoint(), request, VaultWrappedResponse.class);
  }

}

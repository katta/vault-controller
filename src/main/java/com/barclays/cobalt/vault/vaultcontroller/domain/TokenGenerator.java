package com.barclays.cobalt.vault.vaultcontroller.domain;

import com.barclays.cobalt.vault.vaultcontroller.config.VaultConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;

public class TokenGenerator {

  private final RestTemplate http;
  private String vaultBaseUrl;
  private VaultConfiguration configuration;

  public TokenGenerator(RestTemplateBuilder builder, VaultConfiguration configuration) {
    this.configuration = configuration;
    this.http = builder
        .additionalInterceptors(new VaultHttpRequestInterceptor())
        .rootUri(configuration.vaultAPIEndpoint()).build();
  }

  public void generateToken() {

    TokenRequest request = TokenRequest.builder()
        .policies(Collections.singletonList("default"))
        .meta(new HashMap<String, String>() {{
          put("namespace", "4299");
        }})
        .displayName("client")
        .noParent(true)
        .ttl(43200L)
        .renewable(false)
        .numUses(1)
        .build();


    ResponseEntity<WrappedResponse> wrappedToken = http.postForEntity("/auth/token/create", request, WrappedResponse.class);

    System.out.println(wrappedToken.getBody());
  }

}

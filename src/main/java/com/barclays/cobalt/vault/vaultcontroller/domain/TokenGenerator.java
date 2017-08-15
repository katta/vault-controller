package com.barclays.cobalt.vault.vaultcontroller.domain;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

public class TokenGenerator {

  private final RestTemplate http;
  private String vaultBaseUrl;

  public TokenGenerator(RestTemplateBuilder builder) {
    this.vaultBaseUrl = "http://localhost:8200";
    this.http = builder
        .additionalInterceptors(new VaultHttpRequestInterceptor())
        .rootUri("http://localhost:8200/v1").build();
  }

  public void generateToken() {

    TokenRequest request = TokenRequest.builder()
        .withPolicy("default")
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

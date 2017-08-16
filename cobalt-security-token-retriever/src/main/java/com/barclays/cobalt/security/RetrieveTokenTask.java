package com.barclays.cobalt.security;

import com.barclays.cobalt.security.config.ApplicationProperties;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class RetrieveTokenTask implements CommandLineRunner {

  private final RestTemplate http;
  private final ApplicationProperties properties;

  public RetrieveTokenTask(RestTemplateBuilder builder, ApplicationProperties properties) {
    this.properties = properties;
    this.http = builder
        .rootUri(properties.getVaultControllerBaseUri())
        .build();
  }

  @Override
  public void run(String... args) throws Exception {
    String tokenEndpoint = UriComponentsBuilder.fromPath("/token")
        .queryParam("podNamespace", properties.getPodNamespace())
        .queryParam("podName", properties.getPodName())
        .build().toUriString();
    ResponseEntity<Object> status = http.postForEntity(tokenEndpoint, null, Object.class);
    if (!status.getStatusCode().is2xxSuccessful()) {
      throw new IllegalStateException("Failed to initiate request to generate secrets token!");
    }
  }
}

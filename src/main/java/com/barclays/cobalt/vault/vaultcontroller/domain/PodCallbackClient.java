package com.barclays.cobalt.vault.vaultcontroller.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class PodCallbackClient {

  private static final Logger LOGGER = LoggerFactory.getLogger(PodCallbackClient.class);

  private final RestTemplate http;

  public PodCallbackClient(RestTemplateBuilder builder) {
    http = builder.build();
  }

  public void writeToken(String uri, VaultWrappedResponse.WrapInfo wrapInfo) {
    ResponseEntity<Object> responseEntity = http.postForEntity(uri, wrapInfo, Object.class);
    if (responseEntity.getStatusCode() != HttpStatus.CREATED) {
      LOGGER.error("Failed writing token to the pod at {} with status code {}", uri, responseEntity.getStatusCode());
    }
  }
}

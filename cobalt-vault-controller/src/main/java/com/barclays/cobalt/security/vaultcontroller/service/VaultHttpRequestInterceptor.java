package com.barclays.cobalt.security.vaultcontroller.service;

import com.barclays.cobalt.security.vaultcontroller.config.ApplicationProperties.VaultProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

class VaultHttpRequestInterceptor implements ClientHttpRequestInterceptor {

  static final String TOKEN_HEADER = "X-Vault-Token";
  static final String WRAP_RESPONSE_TTL_HEADER = "X-Vault-Wrap-TTL";

  public static final Logger logger = LoggerFactory.getLogger(VaultHttpRequestInterceptor.class);

  private final VaultProperties configuration;

  public VaultHttpRequestInterceptor(VaultProperties configuration) {
    this.configuration = configuration;
  }

  @Override
  public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
    logger.info("Adding vault headers TOKEN: {}", configuration.getRootToken());
    request.getHeaders().add(TOKEN_HEADER, configuration.getRootToken());
    request.getHeaders().add(WRAP_RESPONSE_TTL_HEADER, configuration.getWrapTtl());
    return execution.execute(request, body);
  }
}

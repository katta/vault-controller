package com.barclays.cobalt.vault.vaultcontroller.domain;

import com.barclays.cobalt.vault.vaultcontroller.config.VaultConfiguration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

class VaultHttpRequestInterceptor implements ClientHttpRequestInterceptor {

  static final String TOKEN_HEADER = "X-Vault-Token";
  static final String WRAP_RESPONSE_TTL_HEADER = "X-Vault-Wrap-TTL";

  private final VaultConfiguration configuration;

  public VaultHttpRequestInterceptor(VaultConfiguration configuration) {
    this.configuration = configuration;
  }

  @Override
  public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
    request.getHeaders().add(TOKEN_HEADER, configuration.getRootToken());
    request.getHeaders().add(WRAP_RESPONSE_TTL_HEADER, configuration.getWrapTtl());
    return execution.execute(request, body);
  }
}

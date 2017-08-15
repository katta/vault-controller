package com.barclays.cobalt.vault.vaultcontroller.domain;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

class VaultHttpRequestInterceptor implements ClientHttpRequestInterceptor {
  @Override
  public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
    request.getHeaders().add("X-Vault-Token", "6afb5d96-4e95-6916-e530-94c4d404ebae");
    request.getHeaders().add("X-Vault-Wrap-TTL", "15s");
    return execution.execute(request, body);
  }
}

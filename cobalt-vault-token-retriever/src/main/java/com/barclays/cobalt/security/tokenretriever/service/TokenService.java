package com.barclays.cobalt.security.tokenretriever.service;

import com.barclays.cobalt.security.tokenretriever.config.ApplicationProperties;
import com.barclays.cobalt.security.tokenretriever.domain.TokenResponse;
import com.barclays.cobalt.security.tokenretriever.util.FileUtil;
import org.springframework.http.RequestEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.nio.file.Path;

public class TokenService {

  public static final String VAULT_TOKEN_HEADER = "X-Vault-Token";
  private final RestTemplate http;
  private final URI unwrapEndpoint;
  private Path tokenFilePath;
  private ShutdownService shutdown;

  public TokenService(RestTemplate restTemplate, ShutdownService shutdown, ApplicationProperties properties) {
    http = restTemplate;
    this.unwrapEndpoint = properties.getVault().unwrapEndpoint();
    this.tokenFilePath = properties.tokenFilePath();
    this.shutdown = shutdown;
  }

  @Async
  public void retrieveAndSave(String wrappedToken) {
    String token = retrieve(wrappedToken);
    saveToken(token);
    shutdown.normally();
  }

  private String retrieve(String wrappedToken) {
    RequestEntity<Object> requestEntity = RequestEntity.post(unwrapEndpoint)
        .header(VAULT_TOKEN_HEADER, wrappedToken)
        .body("");

    TokenResponse tokenResponse = http.exchange(requestEntity, TokenResponse.class).getBody();

    return tokenResponse.getAuth().getClientToken();
  }

  private void saveToken(String token) {
    FileUtil.writeSafely(token, tokenFilePath);
  }

}

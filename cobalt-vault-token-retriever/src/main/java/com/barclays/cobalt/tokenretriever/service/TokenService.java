package com.barclays.cobalt.tokenretriever.service;

import com.barclays.cobalt.tokenretriever.config.ApplicationProperties;
import com.barclays.cobalt.tokenretriever.domain.TokenResponse;
import com.barclays.cobalt.tokenretriever.util.FileUtil;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.RequestEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.nio.file.Path;

public class TokenService {

  public static final String VAULT_TOKEN_HEADER = "X-Vault-Token";
  private final RestTemplate http;
  private final URI unwrapEndpoint;
  private Path tokenPath;
  private ShutdownService shutdown;

  public TokenService(RestTemplateBuilder builder, ShutdownService shutdown, ApplicationProperties properties) {
    http = builder.build();
    this.unwrapEndpoint = properties.getVault().unwrapEndpoint();
    this.tokenPath = properties.tokenPath();
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
    FileUtil.writeSafely(token, tokenPath);
  }

}

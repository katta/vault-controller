package com.barclays.cobalt.security.tokenretriever.service;

import com.barclays.cobalt.security.tokenretriever.config.ApplicationProperties;
import com.barclays.cobalt.security.tokenretriever.domain.TokenResponse;
import com.barclays.cobalt.security.tokenretriever.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.RequestEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.nio.file.Path;

public class TokenService {

  public static final Logger logger = LoggerFactory.getLogger(TokenService.class);

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
    logger.info("Retrieving token from vault at {}", unwrapEndpoint);
    RequestEntity<Object> requestEntity = RequestEntity.post(unwrapEndpoint)
        .header(VAULT_TOKEN_HEADER, wrappedToken)
        .body("");

    TokenResponse tokenResponse = http.exchange(requestEntity, TokenResponse.class).getBody();
    logger.info("Client token retrieved from vault successfully. Not logged here for obvious reasons.");

    return tokenResponse.getAuth().getClientToken();
  }

  private void saveToken(String token) {
    logger.info("Writing token from vault at '{}'.", tokenFilePath);
    FileUtil.writeSafely(token, tokenFilePath);
    logger.info("Token from vault successfully written at '{}'.", tokenFilePath);
  }

}

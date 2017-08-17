package com.barclays.cobalt.tokenretriever.domain;

import com.barclays.cobalt.tokenretriever.util.FileUtil;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.RequestEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.nio.file.Path;

public class TokenRetriever {

  static final String VAULT_TOKEN_HEADER = "X-Vault-Token";
  private final RestTemplate http;
  private final URI unwrapEndpoint;
  private Path tokenPath;

  public TokenRetriever(RestTemplateBuilder builder, URI unwrapEndpoint, Path tokenPath) {
    http = builder.build();
    this.unwrapEndpoint = unwrapEndpoint;
    this.tokenPath = tokenPath;
  }

  public void retrieveAndSave(String wrappedToken) {
    saveToken(retrieve(wrappedToken));
  }

  String retrieve(String wrappedToken) {
    RequestEntity<Object> requestEntity = RequestEntity.post(unwrapEndpoint)
        .header(VAULT_TOKEN_HEADER, wrappedToken)
        .body("");

    TokenResponse tokenResponse = http.exchange(requestEntity, TokenResponse.class).getBody();

    return tokenResponse.getAuth().getClientToken();
  }

  void saveToken(String token) {
    FileUtil.writeSafely(token, tokenPath);
  }

}

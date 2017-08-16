package com.barclays.cobalt.security.api;

import com.barclays.cobalt.security.domain.TokenRetriever;
import com.barclays.cobalt.security.domain.WrappedTokenRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenCallbackController {

  private TokenRetriever repository;

  public TokenCallbackController(TokenRetriever repository) {
    this.repository = repository;
  }

  @PostMapping("/token")
  public void saveToken(@RequestBody @Validated WrappedTokenRequest request) {
    repository.retrieveAndSave(request.getToken());
  }

}

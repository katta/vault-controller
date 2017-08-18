package com.barclays.cobalt.security.tokenretriever.api;

import com.barclays.cobalt.security.tokenretriever.service.TokenService;
import com.barclays.cobalt.security.tokenretriever.domain.WrappedTokenRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.ACCEPTED;

@RestController
public class TokenCallbackController {

  private static final Logger logger = LoggerFactory.getLogger(TokenCallbackController.class);

  private TokenService service;

  public TokenCallbackController(TokenService repository) {
    this.service = repository;
  }

  @PostMapping("/token")
  @ResponseStatus(ACCEPTED)
  public void saveToken(@RequestBody @Validated WrappedTokenRequest request) {
    logger.info("Received callback from vault controller");
    service.retrieveAndSave(request.getToken());
  }

}

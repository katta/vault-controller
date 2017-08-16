package com.barclays.cobalt.vault.vaultcontroller.api;


import com.barclays.cobalt.vault.vaultcontroller.service.TokenService;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@RestController
public class TokenController {

  private final TokenService tokenService;

  private static final Logger logger = LoggerFactory.getLogger(TokenController.class);

  public TokenController(TokenService tokenService) {
    this.tokenService = tokenService;
  }

  @PostMapping("/token")
  @ResponseStatus(CREATED)
  public void generateToken(@NotBlank @RequestParam String namespace, @NotBlank @RequestParam String podName) {
    logger.info("Received request to generate token for pod: '{}/{}'", namespace, podName);
    tokenService.generateTokenForPod(namespace, podName);
  }

  @ExceptionHandler
  @ResponseStatus(code = FORBIDDEN)
  public Map<String, String> onException(IllegalArgumentException exception) {
    logger.error("Error encountered when processing generate token request.", exception);
    return Collections.singletonMap("error", exception.getMessage());
  }
}

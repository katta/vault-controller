package com.barclays.cobalt.security.vaultcontroller.api;


import com.barclays.cobalt.security.vaultcontroller.service.TokenService;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@RestController
public class TokenController {

  private final TokenService tokenService;

  private static final Logger logger = LoggerFactory.getLogger(TokenController.class);

  public TokenController(TokenService tokenService) {
    this.tokenService = tokenService;
  }

  @PostMapping("/token")
  @ResponseStatus(ACCEPTED)
  public void generateToken(@NotBlank @RequestParam String podNamespace, @NotBlank @RequestParam String podName) {
    logger.info("Received request to generate token for pod: '{}/{}'", podNamespace, podName);
    tokenService.generateTokenForPod(podNamespace, podName);
  }

  @ExceptionHandler
  @ResponseStatus(code = FORBIDDEN)
  public Map<String, String> onException(IllegalArgumentException exception) {
    logger.error("Error encountered when processing generate token request.", exception);
    return Collections.singletonMap("error", exception.getMessage());
  }
}

package com.barclays.cobalt.vault.vaultcontroller.web;


import com.barclays.cobalt.vault.vaultcontroller.domain.TokenGenerator;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
public class TokenController {

  private final TokenGenerator generator;

  public TokenController(TokenGenerator generator) {
    this.generator = generator;
  }

  @PostMapping("/token")
  @ResponseStatus(CREATED)
  public void generateToken(@NotBlank @RequestParam String namespace, @NotBlank @RequestParam String podName) {
    // validate the pod k8s
    generator.generateToken(podName, namespace, "client", "4299");
    // call init-container (
  }
}

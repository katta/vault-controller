package com.barclays.cobalt.vault.vaultcontroller.web;


import com.barclays.cobalt.vault.vaultcontroller.domain.TokenGenerator;
import org.springframework.web.bind.annotation.PostMapping;
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
  public void generateToken() {
    generator.generateToken();
  }
}

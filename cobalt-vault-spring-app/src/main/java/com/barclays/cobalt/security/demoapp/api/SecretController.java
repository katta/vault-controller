package com.barclays.cobalt.security.demoapp.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecretController {

  @Value("${password}")
  private String password;

  @GetMapping
  public String retrieveSecret() {
    return "Password :" + password;
  }
}

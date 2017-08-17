package com.barclays.cobalt.tokenretriever.domain;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class TokenResponse {
  private Auth auth;

  @Data
  public static class Auth {
    @NotBlank
    private String clientToken;
  }
}
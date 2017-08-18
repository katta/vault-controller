package com.barclays.cobalt.security.tokenretriever.domain;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;


@Data
public class WrappedTokenRequest {

  @NotBlank
  private String token;
}
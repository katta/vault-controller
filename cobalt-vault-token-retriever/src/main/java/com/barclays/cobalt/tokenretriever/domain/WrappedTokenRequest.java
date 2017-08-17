package com.barclays.cobalt.tokenretriever.domain;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;


@Data
public class WrappedTokenRequest {

  @NotBlank
  private String token;
}
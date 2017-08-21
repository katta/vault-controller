package com.barclays.cobalt.security.vaultcontroller.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
public class VaultWrappedResponse {

  private WrapInfo wrapInfo;

  @Data
  @AllArgsConstructor
  public static class WrapInfo {

    private String token;
    private String wrappedAccessor;
    private String creationTime;
  }

}

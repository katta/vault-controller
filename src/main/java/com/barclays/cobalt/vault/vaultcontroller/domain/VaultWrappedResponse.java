package com.barclays.cobalt.vault.vaultcontroller.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@Builder
public class VaultWrappedResponse {

  private WrapInfo wrapInfo;

  @Data
  @AllArgsConstructor
  @Builder
  public static class WrapInfo {

    private String token;
    private String wrappedAccessor;
    private String creationTime;
  }

}

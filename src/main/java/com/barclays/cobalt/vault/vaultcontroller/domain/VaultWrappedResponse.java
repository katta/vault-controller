package com.barclays.cobalt.vault.vaultcontroller.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@Builder
@ToString
public class VaultWrappedResponse {

  private WrapInfo wrapInfo;

  @Data
  @AllArgsConstructor
  @Builder
  private static class WrapInfo {

    private String token;
    private String wrappedAccessor;
    private String creationTime;
  }

}

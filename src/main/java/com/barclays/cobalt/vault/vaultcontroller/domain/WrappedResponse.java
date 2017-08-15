package com.barclays.cobalt.vault.vaultcontroller.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/*
{
  "request_id": "",
  "lease_id": "",
  "renewable": false,
  "lease_duration": 0,
  "data": null,
  "wrap_info": {
    "token": "",
    "ttl": 15,
    "creation_time": "",
    "wrapped_accessor": ""
  },
  "warnings": null,
  "auth": null
}
 */
@Data
@AllArgsConstructor
@Builder
@ToString
public class WrappedResponse {

  @JsonProperty("wrap_info")
  private WrapInfo wrapInfo;

  @Data
  @AllArgsConstructor
  @Builder
  private static class WrapInfo {

    private String token;
    @JsonProperty("wrapped_accessor")
    private String accessor;
    @JsonProperty("creation_time")
    private String creationTime;
  }

}

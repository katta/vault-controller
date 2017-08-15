package com.barclays.cobalt.vault.vaultcontroller.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

/*

{
  "request_id": "",
  "lease_id": "",
  "renewable": false,
  "lease_duration": 0,
  "data": null,
  "wrap_info": {
    "token": "52c42870-9e78-bd92-6083-b6db09b90be4",
    "ttl": 15,
    "creation_time": "2017-08-15T10:57:25.133987696-04:00",
    "wrapped_accessor": "d3252b98-822b-304d-206a-2b9c5576ae82"
  },
  "warnings": null,
  "auth": null
}
 */
public class WrappedResponse {

  @JsonProperty("wrap_info")
  private WrapInfo wrapInfo;

  public WrappedResponse(WrapInfo wrapInfo) {
    this.wrapInfo = wrapInfo;
  }

  @SuppressWarnings("unused")
  private WrappedResponse() {
  }

  private static class WrapInfo {
    private String token;

    @JsonProperty("wrapped_accessor")
    private String accessor;


    private WrapInfo() {
    }

    public String getToken() {
      return token;
    }

    @Override
    public String toString() {
      return token;
    }
  }

  @Override
  public String toString() {
    return wrapInfo.toString();
  }
}

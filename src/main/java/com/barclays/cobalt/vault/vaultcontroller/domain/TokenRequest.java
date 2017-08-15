package com.barclays.cobalt.vault.vaultcontroller.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.TimeUnit;


@Data
@AllArgsConstructor
@Builder
public class TokenRequest {

  private final String id;

  private final List<String> policies;

  private final Map<String, String> meta;

  private final boolean noParent;

  @JsonProperty("no_default_policy")
  private final boolean noDefaultPolicy;

  private final boolean renewable;

  private final long ttl;

  @JsonProperty("explicit_max_ttl")
  private final String explicitMaxTtl;

  @JsonProperty("display_name")
  private final String displayName;

  @JsonProperty("num_uses")
  private final Integer numUses;

}

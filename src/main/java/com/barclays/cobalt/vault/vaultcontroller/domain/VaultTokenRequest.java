package com.barclays.cobalt.vault.vaultcontroller.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.util.Assert;

import java.util.*;

import static java.util.Arrays.asList;


@Data
@AllArgsConstructor
public class VaultTokenRequest {

  private final String id;

  private final Set<String> policies;

  private final Map<String, String> meta;

  private final boolean noParent;

  private final boolean noDefaultPolicy;

  private final boolean renewable;

  private final String ttl;

  private final String explicitMaxTtl;

  private final String displayName;

  private final Integer numUses;

  public static Builder builder() {
    return new Builder();
  }


  public static class Builder {

    private Set<String> policies;
    private Map<String, String> meta;
    private String displayName;
    private long ttlInSeconds;

    private Builder() {
      policies = new LinkedHashSet<>();
      meta = new LinkedHashMap<>();
    }

    public Builder withPolicies(String... policies) {
      this.policies.addAll(asList(policies));
      return this;
    }

    public Builder withMeta(String key, String value) {
      this.meta.put(key, value);
      return this;
    }

    public Builder displayName(String displayName) {
      this.displayName = displayName;
      return this;
    }

    public Builder ttl(long ttlInSeconds) {
      this.ttlInSeconds = ttlInSeconds;
      return this;
    }

    public VaultTokenRequest build() {
      policies.remove("default");
      Assert.isTrue(policies.size() > 0, "Need at least one non-default policy!");
      Assert.isTrue(ttlInSeconds > 0, "Need a positive ttl value!");

      return new VaultTokenRequest(UUID.randomUUID().toString(), policies, meta, true,
          false, false, ttlInSeconds + "s",
          ttlInSeconds + "s", displayName, 1);
    }
  }
}

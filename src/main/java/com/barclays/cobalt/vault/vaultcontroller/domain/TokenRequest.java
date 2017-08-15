package com.barclays.cobalt.vault.vaultcontroller.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.TimeUnit;


public class TokenRequest {

  private final String id;

  private final List<String> policies;

  private final Map<String, String> meta;

  private final Boolean noParent;

  @JsonProperty("no_default_policy")
  private final Boolean noDefaultPolicy;

  private final Boolean renewable;

  private final String ttl;

  @JsonProperty("explicit_max_ttl")
  private final String explicitMaxTtl;

  @JsonProperty("display_name")
  private final String displayName;

  @JsonProperty("num_uses")
  private final Integer numUses;

  TokenRequest(String id, List<String> policies, Map<String, String> meta,
               Boolean noParent, Boolean noDefaultPolicy, Boolean renewable, String ttl,
               String explicitMaxTtl, String displayName, Integer numUses) {

    this.id = id;
    this.policies = policies;
    this.meta = meta;
    this.noParent = noParent;
    this.noDefaultPolicy = noDefaultPolicy;
    this.renewable = renewable;
    this.ttl = ttl;
    this.explicitMaxTtl = explicitMaxTtl;
    this.displayName = displayName;
    this.numUses = numUses;
  }

  /**
   * @return a new {@link Builder}.
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * @return Id of the client token.
   */
  public String getId() {
    return id;
  }

  /**
   * @return policies for the token.
   */
  public List<String> getPolicies() {
    return policies;
  }

  /**
   * @return map of string to string valued metadata, passed through to the audit
   * backends.
   */
  public Map<String, String> getMeta() {
    return meta;
  }

  /**
   * @return {@literal true} if the token should not have the parent.
   */
  public Boolean getNoParent() {
    return noParent;
  }

  /**
   * @return {@literal true} if the default policy should not be be applied.
   */
  public Boolean getNoDefaultPolicy() {
    return noDefaultPolicy;
  }

  /**
   * @return {@literal true} if then the token should be renewable.
   */
  public Boolean getRenewable() {
    return renewable;
  }

  /**
   * @return TTL period of the token.
   */
  public String getTtl() {
    return ttl;
  }

  /**
   * @return explicit TTL of the token.
   */
  public String getExplicitMaxTtl() {
    return explicitMaxTtl;
  }

  /**
   * @return the display name.
   */
  public String getDisplayName() {
    return displayName;
  }

  /**
   * @return the number of allowed token uses.
   */
  public Integer getNumUses() {
    return numUses;
  }

  public static class Builder {

    private String id;

    private List<String> policies = new ArrayList<>();

    private Map<String, String> meta = new LinkedHashMap<>();

    private Boolean noParent;

    private Boolean noDefaultPolicy;

    private Boolean renewable;

    private String ttl;

    private String explicitMaxTtl;

    private String displayName;

    private Integer numUses;

    Builder() {
    }

    /**
     * Configure a the Id of the client token. Can only be specified by a root token.
     * Otherwise, the token Id is a randomly generated UUID.
     *
     * @param id the token Id.
     * @return {@code this} {@link Builder}.
     */
    public Builder id(String id) {
      this.id = id;
      return this;
    }

    /**
     * Configure policies. Replaces previously configured policies.
     *
     * @param policies must not be {@literal null}.
     * @return {@code this} {@link Builder}.
     */
    public Builder policies(Iterable<String> policies) {

      Assert.notNull(policies, "Policies must not be null");

      this.policies = toList(policies);
      return this;
    }

    /**
     * Add a policy.
     *
     * @param policy must not be empty or {@literal null}.
     * @return {@code this} {@link Builder}.
     */
    public Builder withPolicy(String policy) {

      Assert.hasText(policy, "Policy must not be empty");

      this.policies.add(policy);
      return this;
    }

    /**
     * Configure meta. Replaces previously meta.
     *
     * @param meta must not be {@literal null}.
     * @return {@code this} {@link Builder}.
     */
    public Builder meta(Map<String, String> meta) {

      Assert.notNull(meta, "Meta must not be null");

      this.meta = meta;
      return this;
    }

    /**
     * Configure the token to not have the parent token of the caller. This creates a
     * token with no parent. Requires a root caller.
     *
     * @return {@code this} {@link Builder}.
     */
    public Builder noParent() {
      return noParent(true);
    }

    /**
     * Configure the token to not have the parent token of the caller. This creates a
     * token with no parent. Requires a root caller.
     *
     * @param noParent {@literal true} to not have the parent token of the caller.
     * @return {@code this} {@link Builder}.
     */
    public Builder noParent(boolean noParent) {
      this.noParent = noParent;
      return this;
    }

    /**
     * Omit the default policy in the token's policy set
     *
     * @return {@code this} {@link Builder}.
     */
    public Builder noDefaultPolicy() {
      return noDefaultPolicy(true);
    }

    /**
     * Configure whether the default policy should be part of the token's policy set.
     *
     * @param noDefaultPolicy {@literal true} to omit the default policy in the
     *                        token's policy set.
     * @return {@code this} {@link Builder}.
     */
    public Builder noDefaultPolicy(boolean noDefaultPolicy) {
      this.noDefaultPolicy = noDefaultPolicy;
      return this;
    }

    /**
     * Enable TTL extension/renewal for the token.
     *
     * @return {@code this} {@link Builder}.
     */
    public Builder renewable() {
      return renewable(true);
    }

    /**
     * Configure TTL extension/renewal for the token.
     *
     * @param renewable {@literal false} to disable the ability of the token to be
     *                  renewed past its initial TTL. {@literal true}, or omitting this option, will
     *                  allow the token to be renewable up to the system/mount maximum TTL.
     * @return {@code this} {@link Builder}.
     */
    public Builder renewable(boolean renewable) {
      this.renewable = renewable;
      return this;
    }

    /**
     * Configure a TTL (seconds) for the token.
     *
     * @param ttl the time to live in seconds, must not be negative.
     * @return {@code this} {@link Builder}.
     */
    public Builder ttl(long ttl) {
      return ttl(ttl, TimeUnit.SECONDS);
    }

    /**
     * Configure a TTL (seconds) for the token.
     *
     * @param ttl      the time to live, must not be negative.
     * @param timeUnit the time to live, must not be {@literal null}.
     * @return {@code this} {@link Builder}.
     */
    public Builder ttl(long ttl, TimeUnit timeUnit) {

      Assert.isTrue(ttl >= 0, "TTL must not be negative");
      Assert.notNull(timeUnit, "TimeUnit must not be null");

      this.ttl = String.format("%ss", timeUnit.toSeconds(ttl));
      return this;
    }

    /**
     * Configure the explicit maximum TTL (seconds) for the token. This maximum token
     * TTL cannot be changed later, and unlike with normal tokens, updates to the
     * system/mount max TTL value will have no effect at renewal time - the token will
     * never be able to be renewed or used past the value set at issue time.
     *
     * @param explicitMaxTtl the time to live in seconds, must not be negative.
     * @return {@code this} {@link Builder}.
     */
    public Builder explicitMaxTtl(long explicitMaxTtl) {
      return explicitMaxTtl(explicitMaxTtl, TimeUnit.SECONDS);
    }

    /**
     * Configure the explicit maximum TTL for the token. This maximum token TTL cannot
     * be changed later, and unlike with normal tokens, updates to the system/mount
     * max TTL value will have no effect at renewal time - the token will never be
     * able to be renewed or used past the value set at issue time.
     *
     * @param explicitMaxTtl the time to live, must not be negative.
     * @param timeUnit       the time to live, must not be {@literal null}.
     * @return {@code this} {@link Builder}.
     */
    public Builder explicitMaxTtl(long explicitMaxTtl,
                                  TimeUnit timeUnit) {

      Assert.isTrue(explicitMaxTtl >= 0, "TTL must not be negative");
      Assert.notNull(timeUnit, "TimeUnit must not be null");

      this.explicitMaxTtl = String
          .format("%ss", timeUnit.toSeconds(explicitMaxTtl));
      return this;
    }

    /**
     * Configure the maximum uses for the token. This can be used to create a
     * one-time-token or limited use token. Defaults to {@literal 0}, which has no
     * limit to the number of uses.
     *
     * @param numUses number of uses, must not be negative.
     * @return {@code this} {@link Builder}.
     */
    public Builder numUses(int numUses) {

      Assert.isTrue(numUses >= 0, "Number of uses must not be negative");

      this.numUses = numUses;
      return this;
    }

    /**
     * Configure a display name for the token, defaults to "token".
     *
     * @param displayName must not be empty or {@literal null}.
     * @return {@code this} {@link Builder}.
     */
    public Builder displayName(String displayName) {

      Assert.hasText(displayName, "Display name must not be empty");

      this.displayName = displayName;
      return this;
    }

    /**
     * Build a new {@link TokenRequest} instance.
     *
     * @return a new {@link TokenRequest}.
     */
    public TokenRequest build() {

      List<String> policies;
      switch (this.policies.size()) {
        case 0:
          policies = Collections.emptyList();
          break;
        case 1:
          policies = Collections.singletonList(this.policies.get(0));
          break;
        default:
          policies = Collections.unmodifiableList(new ArrayList<>(
              this.policies));

      }
      Map<String, String> meta;
      switch (this.meta.size()) {
        case 0:
          meta = Collections.emptyMap();
          break;
        default:
          meta = Collections
              .unmodifiableMap(new LinkedHashMap<>(this.meta));
      }

      return new TokenRequest(id, policies, meta, noParent, noDefaultPolicy,
          renewable, ttl, explicitMaxTtl, displayName, numUses);
    }

    private static <E> List<E> toList(Iterable<E> iter) {

      List<E> list = new ArrayList<E>();
      for (E item : iter) {
        list.add(item);
      }

      return list;
    }
  }
}

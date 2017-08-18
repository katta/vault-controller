package com.barclays.cobalt.security.vaultcontroller.domain;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Enclosed.class)
public class VaultTokenRequestTest {

  public static class InvalidValues {
    @Rule
    public ExpectedException thrown= ExpectedException.none();

    @Test
    public void shouldNotBuildWithZeroTtl() throws Exception {
      thrown.expect(IllegalArgumentException.class);
      VaultTokenRequest.builder().withPolicies("non-default").ttl(0).build();
    }

    @Test
    public void shouldHaveAtleastOneNonDefaultPolicy() throws Exception {
      thrown.expect(IllegalArgumentException.class);
      VaultTokenRequest.builder().withPolicies("default").ttl(1).build();
    }
  }

  public static class ValidValues {

    private VaultTokenRequest request;

    @Before
    public void setUp() throws Exception {
      request = VaultTokenRequest.builder().withPolicies("non-default").ttl(1).build();
    }

    @Test
    public void shouldCreateWithValidTtl() throws Exception {
      assertThat(request.getTtl()).isEqualTo("1s");
    }

    @Test
    public void shouldCreateWithValidPolicy() throws Exception {
      assertThat(request.getPolicies()).hasSize(1).contains("non-default");
    }

    @Test
    public void shouldCreateForSingleUse() throws Exception {
      assertThat(request.getNumUses()).isEqualTo(1);
    }

    @Test
    public void shouldNotBeRenewable() throws Exception {
      assertThat(request.isRenewable()).isFalse();
    }

    @Test
    public void shouldNotHaveParent() throws Exception {
      assertThat(request.isNoParent()).isTrue();
    }


    @Test
    public void shouldAddMetadata() throws Exception {
      request = VaultTokenRequest.builder().withPolicies("non-default").ttl(1).withMeta("key", "value").build();
      assertThat(request.getMeta()).containsKeys("key").containsValues("value").hasSize(1);
    }
  }
}
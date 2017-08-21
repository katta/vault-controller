package com.barclays.cobalt.security.demoapp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = {
    "vault.token.file=classpath:/test-vault-token"
})
public class ApplicationTests {

  @Test
  public void contextLoads() {
    // TODO: Enhance to simulate vault
  }

}

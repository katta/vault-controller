package com.barclays.cobalt.security.tokenretriever;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = {
    "application.token.retrieval.enabled=false"
})
public class ApplicationTests {

  @Test
  public void contextLoads() {
  }

}

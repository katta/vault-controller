package com.barclays.cobalt.vault.vaultcontroller.domain;

import com.barclays.cobalt.vault.vaultcontroller.config.OpenshiftProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class OpenshiftClientTest {

  @Autowired
  private OpenshiftProperties openshiftProperties;

  @Test
  public void shouldFindPodsBySelector() {
    PodMetadata podMetadata = new OpenshiftClient(openshiftProperties).findPod("spring-example", "spring-example-1-x4tmw");
    System.out.println(podMetadata);
  }

}
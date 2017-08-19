package com.barclays.cobalt.security.vaultcontroller.api;

import com.barclays.cobalt.security.vaultcontroller.service.TokenService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class TokenControllerTest {

  private MockMvc mvc;

  @Mock
  private TokenService tokenService;

  @InjectMocks
  private TokenController controller;

  @Before
  public void setUp() throws Exception {
    mvc = MockMvcBuilders
        .standaloneSetup(controller)
        .build();
  }

  @Test
  public void shouldGenerateToken() throws Exception {
    String namespace = "my-namespace";
    String podName = "my-awesome-pod";

    mvc.perform(post("/token")
        .param("podNamespace", namespace)
        .param("podName", podName))
        .andExpect(status().isAccepted());

    verify(tokenService).generateTokenForPod(namespace, podName);
  }

  @Test
  public void shouldBeInvalidIfPodNameIsMissing() throws Exception {
    mvc.perform(post("/token")
        .param("podNamespace", "4299"))
        .andExpect(status().isBadRequest());

    verifyZeroInteractions(tokenService);
  }

  @Test
  public void shouldBeInvalidIfNamespaceIsMissing() throws Exception {
    mvc.perform(post("/token")
        .param("podName", "init-container"))
        .andExpect(status().isBadRequest());

    verifyZeroInteractions(tokenService);
  }
}
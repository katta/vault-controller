package com.barclays.cobalt.vaultcontroller.api;

import com.barclays.cobalt.vaultcontroller.service.TokenService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TokenController.class)
public class TokenControllerTest {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private TokenService tokenService;

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
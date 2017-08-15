package com.barclays.cobalt.vault.vaultcontroller.web;

import com.barclays.cobalt.vault.vaultcontroller.domain.TokenGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TokenController.class)
public class TokenControllerTest {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private TokenGenerator tokenGenerator;

  @Test
  public void shouldGenerateToken() throws Exception {
    mvc.perform(post("/token")
        .param("namespace", "4299")
        .param("podName", "init-container"))
        .andExpect(status().isCreated());
  }

  @Test
  public void shouldBeInvalidIfPodNameIsMissing() throws Exception {
    mvc.perform(post("/token")
        .param("namespace", "4299"))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void shouldBeInvalidIfNamespaceIsMissing() throws Exception {
    mvc.perform(post("/token")
        .param("podName", "init-container"))
        .andExpect(status().isBadRequest());
  }
}
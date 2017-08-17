package com.barclays.cobalt.tokenretriever.api;

import com.barclays.cobalt.tokenretriever.service.TokenService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class TokenCallbackControllerTest {

  @Mock
  private TokenService tokenService;

  @InjectMocks
  private TokenCallbackController controller;
  private MockMvc mockMvc;

  @Before
  public void setUp() throws Exception {
    mockMvc = MockMvcBuilders
        .standaloneSetup(controller)
        .build();
  }

  @Test
  public void shouldReceiveWrappedToken() throws Exception {
    mockMvc.perform(post("/token")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"token\": \"test-token-value\"}"))
        .andExpect(status().isAccepted());

    verify(tokenService).retrieveAndSave("test-token-value");
  }

  @Test
  public void shouldRejectBlankToken() throws Exception {
    mockMvc.perform(post("/token")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"token\": \"\"}"))
        .andExpect(status().isBadRequest());

    verifyZeroInteractions(tokenService);
  }
}
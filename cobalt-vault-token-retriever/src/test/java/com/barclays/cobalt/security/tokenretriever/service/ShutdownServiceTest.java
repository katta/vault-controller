package com.barclays.cobalt.security.tokenretriever.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.ExitCodeEvent;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Timer;
import java.util.TimerTask;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(MockitoJUnitRunner.class)
public class ShutdownServiceTest {

  @Mock
  private ConfigurableApplicationContext context;
  @Mock
  private Timer timer;

  private ShutdownService shutdown;

  @Before
  public void setUp() throws Exception {
    shutdown = new ShutdownService(context, 10, timer);
  }

  @Test
  public void shouldExitWithCodeZeroWhenExitingNormally() throws Exception {
    shutdown.normally();

    ArgumentCaptor<ExitCodeEvent> exitEvent = forClass(ExitCodeEvent.class);

    verify(context).close();
    verify(context).publishEvent(exitEvent.capture());
    assertThat(exitEvent.getValue().getExitCode()).isEqualTo(0);
    verifyZeroInteractions(timer);
  }

  @Test
  public void shouldExitWithNonZeroCodeZeroWhenExitingAbnormally() throws Exception {
    shutdown.abnormally();

    ArgumentCaptor<ExitCodeEvent> exitEvent = forClass(ExitCodeEvent.class);

    verify(context).close();
    verify(context).publishEvent(exitEvent.capture());
    assertThat(exitEvent.getValue().getExitCode()).isNotEqualTo(0);
    verifyZeroInteractions(timer);
  }

  @Test
  public void shouldScheduleExitWithWhenExitingDelayed() throws Exception {
    shutdown.delayed();

    verify(timer).schedule(any(TimerTask.class), eq(10_000L));
  }
}
package io.cosmosoftware.kite.janus.steps.streaming;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.interfaces.Runner;
import io.cosmosoftware.kite.janus.pages.JanusPage;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.WebDriver;

public class JoinStreamingStep extends TestStep {

  private final String streamSet;
  private final JanusPage janusPage;
  
  public JoinStreamingStep(Runner runner, String streamSet) {
    super(runner);
    this.streamSet = streamSet;
    this.janusPage = new JanusPage(runner);
  }

  @Override
  protected void step() throws KiteTestException {
    janusPage.openStreamSetList();
    janusPage.selectStreamSet(this.streamSet);
    janusPage.launchStreaming();
  }

  @Override
  public String stepDescription() {
    return "Select a stream set and watch or listen it";
  }
}

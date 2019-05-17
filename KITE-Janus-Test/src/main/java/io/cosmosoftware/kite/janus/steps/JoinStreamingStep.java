package io.cosmosoftware.kite.janus.steps;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.WebDriver;

public class JoinStreamingStep extends TestStep {

  private final String streamSet;

  public JoinStreamingStep(WebDriver webDriver, String streamSet) {
    super(webDriver);
    this.streamSet = streamSet;
  }

  @Override
  protected void step() throws KiteTestException {


  }

  @Override
  public String stepDescription() {
    return "Select a stream set and watch or listen it";
  }
}

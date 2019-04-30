package io.cosmosoftware.kite.jitsi.checks;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.WebDriver;

public class RemoteVideoCheck extends TestStep {
  RemoteVideoCheck(WebDriver webDriver){
    super(webDriver);
  }

  @Override
  public String stepDescription() {
    return "Video Check";
  }

  @Override
  protected void step() throws KiteTestException {

  }
}

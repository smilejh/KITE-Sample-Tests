package io.cosmosoftware.kite.janus.steps;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.janus.pages.JanusPage;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.WebDriver;

public class LeaveDemoStep extends TestStep {
  public LeaveDemoStep(WebDriver webDriver) {
    super(webDriver);
  }

  @Override
  protected void step() throws KiteTestException {
    final JanusPage janusPage = new JanusPage(this.webDriver, logger);

    janusPage.startOrStopDemo();

  }

  @Override
  public String stepDescription() {
    return "Leave the janus demo test";
  }
}

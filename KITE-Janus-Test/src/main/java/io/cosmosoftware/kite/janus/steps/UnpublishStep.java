package io.cosmosoftware.kite.janus.steps;

import io.cosmosoftware.kite.exception.KiteInteractionException;
import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.janus.pages.JanusPage;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.WebDriver;

public class UnpublishStep extends TestStep {

  private JanusPage janusPage;

  public UnpublishStep(WebDriver webDriver) {
    super(webDriver);
    janusPage = new JanusPage(webDriver);
  }

  @Override
  public String stepDescription() {
    return "Stop publishing video";
  }

  @Override
  protected void step() throws KiteInteractionException {
    janusPage.unpublish();
  }
  
}

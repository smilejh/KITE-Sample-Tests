package io.cosmosoftware.kite.janus.steps;

import io.cosmosoftware.kite.exception.KiteInteractionException;
import io.cosmosoftware.kite.janus.pages.JanusPage;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.WebDriver;

public class UnpublishStep extends TestStep {


  public UnpublishStep(WebDriver webDriver) {
    super(webDriver);
  }

  @Override
  public String stepDescription() {
    return "Stop publishing video";
  }

  @Override
  protected void step() throws KiteInteractionException {
    final JanusPage janusPage = new JanusPage(this.webDriver, this.logger);
    janusPage.unpublish();
  }
  
}

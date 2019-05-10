package io.cosmosoftware.kite.janus.steps;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.janus.pages.DemoJanusPage;
import io.cosmosoftware.kite.janus.pages.JanusPage;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.WebDriver;

public class JoinVideoCallStep extends TestStep {
  
  private final String url;

  
  public JoinVideoCallStep(WebDriver webDriver, String url) {
    super(webDriver);
    this.url = url;
  }
  
  @Override
  public String stepDescription() {
    return "Open " + url + " click on start button";
  }
  
  @Override
  protected void step() throws KiteTestException {
    final DemoJanusPage echoJanusPage = new DemoJanusPage(this.webDriver, this.logger);
    echoJanusPage.load(url);
    echoJanusPage.joinCall();

  }
}

package io.cosmosoftware.kite.janus.steps;

import io.cosmosoftware.kite.janus.pages.JanusPage;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.WebDriver;

public class JoinVideoCallStep extends TestStep {



  private final JanusPage janusPage = new JanusPage(this.webDriver);
  private final String url;

  
  public JoinVideoCallStep(WebDriver webDriver, String url) {
    super(webDriver);
    this.url = url;
  }
  
  @Override
  public String stepDescription() {
    return "Open " + url;
  }
  
  @Override
  protected void step() {
    janusPage.setLogger(logger);
    janusPage.load(url);
  }
}

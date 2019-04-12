package io.cosmosoftware.kite.simulcast.steps;

import io.cosmosoftware.kite.simulcast.pages.JanusLoopbackPage;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.WebDriver;

import static io.cosmosoftware.kite.entities.Timeouts.ONE_SECOND_INTERVAL;
import static io.cosmosoftware.kite.util.TestUtils.waitAround;
import static io.cosmosoftware.kite.util.WebDriverUtils.loadPage;

public class LoadPageStep extends TestStep {

  private final String url;

  
  public LoadPageStep(WebDriver webDriver, String url) {
    super(webDriver);
    this.url = url;
  }
  
  @Override
  public String stepDescription() {
    return "Open " + url;
  }
  
  @Override
  protected void step() {
    loadPage(webDriver, url, 20);
    waitAround(ONE_SECOND_INTERVAL);
    if (url.contains("meetecho")) {
      new JanusLoopbackPage(webDriver).clickButton();
    }
  }
}

package io.cosmosoftware.kite.janus.steps;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.interfaces.Runner;
import io.cosmosoftware.kite.janus.pages.JanusPage;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.WebDriver;

import static io.cosmosoftware.kite.entities.Timeouts.ONE_SECOND_INTERVAL;
import static io.cosmosoftware.kite.util.TestUtils.waitAround;
import static io.cosmosoftware.kite.util.WebDriverUtils.loadPage;

public class StartDemoStep extends TestStep {
  
  private final String url;
  private final JanusPage janusPage;

  
  public StartDemoStep(Runner runner, String url) {
    super(runner);
    this.url = url;
    this.janusPage = new JanusPage(runner);
  }
  
  @Override
  public String stepDescription() {
    return "Open " + url + " and click on start button to join the demo";
  }
  
  @Override
  protected void step() throws KiteTestException {
    loadPage(this.webDriver, url, 10);
    waitAround(3*ONE_SECOND_INTERVAL);
    janusPage.startOrStopDemo();

  }
}

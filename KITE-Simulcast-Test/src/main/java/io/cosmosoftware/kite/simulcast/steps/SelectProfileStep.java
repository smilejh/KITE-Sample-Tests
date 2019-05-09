package io.cosmosoftware.kite.simulcast.steps;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.simulcast.pages.SimulcastPageBase;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.WebDriver;

import static io.cosmosoftware.kite.entities.Timeouts.ONE_SECOND_INTERVAL;
import static io.cosmosoftware.kite.util.TestUtils.waitAround;

public class SelectProfileStep extends TestStep {

  private final SimulcastPageBase loopbackPage;

  private final String rid;
  private final int tid;

  public SelectProfileStep(WebDriver webDriver, SimulcastPageBase page, String rid, int tid) {
    super(webDriver);
    this.loopbackPage = page;
    this.rid = rid;
    this.tid = tid;
  }
  
  @Override
  public String stepDescription() {
    return "Clicking button " + rid + tid;
  }
  
  @Override
  protected void step() throws KiteTestException {
    loopbackPage.clickButton(rid, tid);
    waitAround(3 * ONE_SECOND_INTERVAL);
  }
}

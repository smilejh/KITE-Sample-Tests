package io.cosmosoftware.kite.janus.steps;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.janus.pages.JanusPage;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.WebDriver;

import static io.cosmosoftware.kite.entities.Timeouts.ONE_SECOND_INTERVAL;
import static io.cosmosoftware.kite.util.TestUtils.waitAround;

public class SelectProfileStep extends TestStep {

  private final JanusPage janusPage;

  private final String rid; //select quality of the encoded video
  private final int tid; //Cap the temporal layer frame FPS

  public SelectProfileStep(WebDriver webDriver, JanusPage page, String rid, int tid) {
    super(webDriver);
    this.janusPage = page;
    this.rid = rid;
    this.tid = tid;
  }
  
  @Override
  public String stepDescription() {
    return "Clicking button " + rid + tid;
  }
  
  @Override
  protected void step() throws KiteTestException {
    janusPage.clickButton(rid, tid);
    waitAround(3 * ONE_SECOND_INTERVAL);
  }
}

package io.cosmosoftware.kite.simulcast.checks;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.report.Reporter;
import io.cosmosoftware.kite.report.Status;
import io.cosmosoftware.kite.simulcast.pages.JanusLoopbackPage;
import io.cosmosoftware.kite.simulcast.pages.SimulcastPageBase;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.WebDriver;

import static io.cosmosoftware.kite.entities.Timeouts.ONE_SECOND_INTERVAL;
import static io.cosmosoftware.kite.util.TestUtils.waitAround;

public class BandwidthCheck extends TestStep {

  private final JanusLoopbackPage loopbackPage;

  private final int cap;
  private final int duration;

  public BandwidthCheck(WebDriver webDriver, SimulcastPageBase page, int cap, int duration) {
    super(webDriver);
    this.loopbackPage = (JanusLoopbackPage)page;
    this.cap = cap;
    this.duration = duration;
  }
  
  @Override
  public String stepDescription() {
    return "Bandwidth Check with cap at " + cap + "bps";
  }
  
  @Override
  protected void step() throws KiteTestException {
    int nbLowHigherThanMedium = 0;
    int nbMediumHigherThanHigh = 0;
    String result = "";
    loopbackPage.setBitrateCap("" + cap);
    for (int i = 0; i < duration; i++) {
      if (loopbackPage.lowHigherThanMedium()) {
        nbLowHigherThanMedium++;
      }
      if (loopbackPage.mediumHigherThanHigh()){
        nbMediumHigherThanHigh++;
      }
      result = "nbLowHigherThanMedium = " + nbLowHigherThanMedium + ", nbMediumHigherThanHigh = "
        + nbMediumHigherThanHigh + " [" + i + "/" + duration + "]";
      logger.info(result);
      waitAround(ONE_SECOND_INTERVAL);
    }
    Reporter.getInstance().textAttachment(report, "Bandwidth Check", result, "plain");
    if (nbLowHigherThanMedium > 0 || nbMediumHigherThanHigh > 0) {
      throw new KiteTestException("Bandwidth check failed:  " + result, Status.FAILED, null, true);
    }
  }
}

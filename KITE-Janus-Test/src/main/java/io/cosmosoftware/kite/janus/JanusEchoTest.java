package io.cosmosoftware.kite.janus;

import io.cosmosoftware.kite.janus.checks.AllVideoCheck;
import io.cosmosoftware.kite.janus.checks.FirstVideoCheck;
import io.cosmosoftware.kite.janus.steps.GetStatsStep;
import io.cosmosoftware.kite.janus.steps.JoinVideoCallStep;
import io.cosmosoftware.kite.janus.steps.ScreenshotStep;
import org.openqa.selenium.WebDriver;
import org.webrtc.kite.tests.TestRunner;

import static org.webrtc.kite.Utils.getStackTrace;

public class JanusEchoTest extends KiteJanusTest {


  @Override
  public void populateTestSteps(TestRunner runner) {
    try{
    WebDriver webDriver = runner.getWebDriver();
    runner.addStep(new JoinVideoCallStep(webDriver, this.url));
    runner.addStep(new FirstVideoCheck(webDriver));
    runner.addStep(new AllVideoCheck(webDriver, getMaxUsersPerRoom()));
    if (this.getStats()) {
      runner.addStep(
          new GetStatsStep(
              webDriver,
              getMaxUsersPerRoom(),
              getStatsCollectionTime(),
              getStatsCollectionInterval(),
              getSelectedStats()));
    }
    if (this.takeScreenshotForEachTest()) {
      runner.addStep(new ScreenshotStep(webDriver));
    }
  } catch (Exception e){
    logger.error(getStackTrace(e));
  }
  }


  @Override
  public void payloadHandling () {
    super.payloadHandling();
  }
}


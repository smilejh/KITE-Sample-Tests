package io.cosmosoftware.kite.janus;

import io.cosmosoftware.kite.janus.checks.FirstVideoCheck;

import io.cosmosoftware.kite.janus.steps.GetStatsStep;
import io.cosmosoftware.kite.janus.steps.ScreenshotStep;
import io.cosmosoftware.kite.janus.steps.StartDemoStep;
import io.cosmosoftware.kite.simulcast.checks.ReceiverVideoCheck;
import org.openqa.selenium.WebDriver;
import org.webrtc.kite.tests.KiteBaseTest;
import org.webrtc.kite.tests.TestRunner;

import static org.webrtc.kite.Utils.getStackTrace;

public class JanusStreamingTest extends KiteBaseTest {
  @Override
  protected void populateTestSteps(TestRunner runner) {
    try {
      WebDriver webDriver = runner.getWebDriver();
      runner.addStep(new StartDemoStep(webDriver, this.url));
      runner.addStep(new StartDemoStep(webDriver, this.url));
      runner.addStep(new FirstVideoCheck(webDriver));
      runner.addStep(new ScreenshotStep(webDriver));
      runner.addStep(new ReceiverVideoCheck(webDriver));

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

    } catch(Exception e){
      logger.error(getStackTrace(e));
    }


  }
}

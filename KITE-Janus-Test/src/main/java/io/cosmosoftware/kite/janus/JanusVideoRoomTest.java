package io.cosmosoftware.kite.janus;

import io.cosmosoftware.kite.janus.checks.AllVideoCheck;
import io.cosmosoftware.kite.janus.checks.FirstVideoCheck;
import io.cosmosoftware.kite.janus.steps.GetStatsStep;
import io.cosmosoftware.kite.janus.steps.JoinVideoRoomStep;
import io.cosmosoftware.kite.janus.steps.ScreenshotStep;
import io.cosmosoftware.kite.janus.steps.StartDemoStep;
import io.cosmosoftware.kite.util.TestUtils;
import org.openqa.selenium.WebDriver;
import org.webrtc.kite.tests.KiteBaseTest;
import org.webrtc.kite.tests.TestRunner;

import static org.webrtc.kite.Utils.getStackTrace;



public class JanusVideoRoomTest extends KiteBaseTest {

  private final String demoName = "sfutest";

  @Override
  protected void populateTestSteps(TestRunner runner) {
    try {
      WebDriver webDriver = runner.getWebDriver();
      String userName = "user" + TestUtils.idToString(runner.getId());
      runner.addStep(new StartDemoStep(webDriver, this.url));
      runner.addStep(new JoinVideoRoomStep(webDriver, userName));
      runner.addStep(new FirstVideoCheck(webDriver));
      runner.addStep(new AllVideoCheck(webDriver, getMaxUsersPerRoom()));
      if (this.getStats()) {
        runner.addStep(
            new GetStatsStep(
                webDriver,
                getMaxUsersPerRoom(),
                getStatsCollectionTime(),
                getStatsCollectionInterval(),
                getSelectedStats(), demoName));
      }
      if (this.takeScreenshotForEachTest()) {
        runner.addStep(new ScreenshotStep(webDriver));
      }
    } catch (Exception e) {
      logger.error(getStackTrace(e));
    }
  }

}

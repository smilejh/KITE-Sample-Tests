package io.cosmosoftware.kite.jitsi;

import io.cosmosoftware.kite.jitsi.checks.AllVideoCheck;
import io.cosmosoftware.kite.jitsi.checks.FirstVideoCheck;
import io.cosmosoftware.kite.jitsi.steps.GetStatsStep;
import io.cosmosoftware.kite.jitsi.steps.JoinRoomStep;
import io.cosmosoftware.kite.jitsi.steps.ScreenshotStep;
import org.openqa.selenium.WebDriver;
import org.webrtc.kite.tests.KiteBaseTest;
import org.webrtc.kite.tests.TestRunner;

import static org.webrtc.kite.Utils.getStackTrace;

public class KiteJitsiTest extends KiteBaseTest {
  private String roomId;

  @Override
  protected void populateTestSteps(TestRunner runner) {
    try {
      WebDriver webDriver = runner.getWebDriver();
      if (runner.getId() % getMaxUsersPerRoom() == 0) {
        generateRoomId();
      }
      runner.addStep(new JoinRoomStep(webDriver, url, roomId));
      runner.addStep(new FirstVideoCheck(webDriver));
      runner.addStep(new AllVideoCheck(webDriver));
      if (this.getStats()) {
        runner.addStep(
            new GetStatsStep(
                webDriver,
                getStatsCollectionTime(),
                getStatsCollectionInterval(),
                getSelectedStats()));
      }
      if (this.takeScreenshotForEachTest()) {
        runner.addStep(new ScreenshotStep(webDriver));
      }

    } catch (Exception e) {
      logger.error(getStackTrace(e));
    }
  }

  private void generateRoomId() {
    this.roomId = Integer.toString(getRandomInt(1000000));
  }

  private int getRandomInt(int max) {
    return (int) Math.floor(Math.random() * Math.floor(max));
  }

}

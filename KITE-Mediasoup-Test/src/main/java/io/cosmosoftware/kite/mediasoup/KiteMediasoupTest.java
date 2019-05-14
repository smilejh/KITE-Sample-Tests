package io.cosmosoftware.kite.mediasoup;

import io.cosmosoftware.kite.mediasoup.checks.AllVideoCheck;
import io.cosmosoftware.kite.mediasoup.checks.FirstVideoCheck;
import io.cosmosoftware.kite.mediasoup.steps.*;
import org.openqa.selenium.WebDriver;
import org.webrtc.kite.tests.KiteBaseTest;
import org.webrtc.kite.tests.TestRunner;

import javax.json.JsonObject;

import static org.webrtc.kite.Utils.getStackTrace;

public class KiteMediasoupTest extends KiteBaseTest {

  private int loadReachTime = 0;

  @Override
  protected void payloadHandling() {
    super.payloadHandling();
    JsonObject jsonPayload = this.payload;
    if (jsonPayload != null) {
      loadReachTime = jsonPayload.getInt("loadReachTime", loadReachTime);
      setExpectedTestDuration(Math.max(getExpectedTestDuration(), (loadReachTime + 300) / 60));
    }
  }

  @Override
  public void populateTestSteps(TestRunner runner) {
    try {
      WebDriver webDriver = runner.getWebDriver();
      runner.addStep(new JoinVideoCallStep(webDriver, getRoomManager().getRoomUrl()));
      runner.addStep(new SetUserId(webDriver, "user" + runner.getId()));
      if (!this.fastRampUp()) {
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
        if (this.loadReachTime > 0) {
          runner.addStep(new StayInMeetingStep(webDriver, loadReachTime));
        }
      }
    } catch (Exception e) {
      logger.error(getStackTrace(e));
    }
  }
}

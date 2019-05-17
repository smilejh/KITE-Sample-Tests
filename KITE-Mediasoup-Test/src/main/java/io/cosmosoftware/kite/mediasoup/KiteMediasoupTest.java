package io.cosmosoftware.kite.mediasoup;

import io.cosmosoftware.kite.mediasoup.checks.AllVideoCheck;
import io.cosmosoftware.kite.mediasoup.checks.FirstVideoCheck;
import io.cosmosoftware.kite.mediasoup.steps.GetStatsStep;
import io.cosmosoftware.kite.mediasoup.steps.JoinVideoCallStep;
import io.cosmosoftware.kite.mediasoup.steps.SetUserIdStep;
import io.cosmosoftware.kite.mediasoup.steps.StartGetStatsSDKStep;
import org.openqa.selenium.WebDriver;
import org.webrtc.kite.steps.ScreenshotStep;
import org.webrtc.kite.steps.StayInMeetingStep;
import org.webrtc.kite.tests.KiteBaseTest;
import org.webrtc.kite.tests.TestRunner;

import javax.json.JsonObject;

import static org.webrtc.kite.Utils.getStackTrace;

public class KiteMediasoupTest extends KiteBaseTest {

  private JsonObject getStatsSdk;

  @Override
  protected void payloadHandling() {
    super.payloadHandling();
    if (this.payload != null) {
      getStatsSdk = this.payload.getJsonObject("getStatsSdk");
    }
  }

  @Override
  public void populateTestSteps(TestRunner runner) {
    try {
      WebDriver webDriver = runner.getWebDriver();
      runner.addStep(new JoinVideoCallStep(webDriver, getRoomManager().getRoomUrl()));
      runner.addStep(new SetUserIdStep(webDriver, "user" + runner.getId()));
      if (!this.fastRampUp()) {
        runner.addStep(new FirstVideoCheck(webDriver));
        if (this.getStatsSdk != null) {
          runner.addStep(new StartGetStatsSDKStep(runner.getWebDriver(), this.name, this.getStatsSdk));
        }
        runner.addStep(new AllVideoCheck(webDriver, getMaxUsersPerRoom()));
        if (this.getStats()) {
          runner.addStep(new GetStatsStep( webDriver, getStatsConfig));
        }
        if (this.takeScreenshotForEachTest()) {
          runner.addStep(new ScreenshotStep(webDriver));
        }
        if (this.meetingDuration > 0) {
          runner.addStep(new StayInMeetingStep(webDriver, meetingDuration));
        }
      }
    } catch (Exception e) {
      logger.error(getStackTrace(e));
    }
  }
}
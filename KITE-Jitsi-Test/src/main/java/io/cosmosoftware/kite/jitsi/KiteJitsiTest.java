package io.cosmosoftware.kite.jitsi;

import io.cosmosoftware.kite.jitsi.checks.AllVideoCheck;
import io.cosmosoftware.kite.jitsi.checks.FirstVideoCheck;
import io.cosmosoftware.kite.jitsi.steps.*;
import org.openqa.selenium.WebDriver;
import org.webrtc.kite.tests.KiteBaseTest;
import org.webrtc.kite.tests.TestRunner;

import static org.webrtc.kite.Utils.getStackTrace;


import javax.json.JsonObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class KiteJitsiTest extends KiteBaseTest {
  
  private JsonObject getStatsSdk;

  @Override
  protected void payloadHandling() {
    super.payloadHandling();
    if (this.payload != null) {
      getStatsSdk = this.payload.getJsonObject("getStatsSdk");
    }
  }

  @Override
  protected void populateTestSteps(TestRunner runner) {
    try {
      WebDriver webDriver = runner.getWebDriver();
      runner.addStep(new JoinRoomStep(webDriver, getRoomManager().getRoomUrl()));
      runner.addStep(new FirstVideoCheck(webDriver));
      if (this.getStatsSdk != null) {
        runner.addStep(new StartGetStatsSDKStep(runner.getWebDriver(), this.name, getStatsSdk));
      }
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
      if (this.meetingDuration > 0) {
        runner.addStep(new StayInMeetingStep(webDriver, meetingDuration));
      }
      
    } catch (Exception e) {
      logger.error(getStackTrace(e));
    }
  }
}

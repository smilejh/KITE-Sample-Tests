package io.cosmosoftware.kite.jitsi;

import io.cosmosoftware.kite.jitsi.checks.AllVideoCheck;
import io.cosmosoftware.kite.jitsi.checks.FirstVideoCheck;
import io.cosmosoftware.kite.jitsi.steps.GetStatsStep;
import io.cosmosoftware.kite.jitsi.steps.JoinRoomStep;
import io.cosmosoftware.kite.jitsi.steps.SetUserIdStep;
import io.cosmosoftware.kite.jitsi.steps.StartGetStatsSDKStep;
import io.cosmosoftware.kite.steps.ScreenshotStep;
import io.cosmosoftware.kite.steps.StayInMeetingStep;
import org.webrtc.kite.tests.KiteBaseTest;
import org.webrtc.kite.tests.TestRunner;

import javax.json.JsonObject;

import static org.webrtc.kite.Utils.getStackTrace;

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
      runner.addStep(new JoinRoomStep(runner, getRoomManager().getRoomUrl()));
      runner.addStep(new SetUserIdStep(runner, "user" + runner.getId()));
      runner.addStep(new FirstVideoCheck(runner));
      runner.addStep(new AllVideoCheck(runner, getMaxUsersPerRoom()));
      if (this.getStats()) {
        runner.addStep(new GetStatsStep( runner, getStatsConfig));
      }
      if (this.getStatsSdk != null) {
        runner.addStep(new StartGetStatsSDKStep(runner, this.name, getStatsSdk));
      }
      if (this.takeScreenshotForEachTest()) {
        runner.addStep(new ScreenshotStep(runner));
      }
      if (this.meetingDuration > 0) {
        runner.addStep(new StayInMeetingStep(runner, meetingDuration));
      }
      
    } catch (Exception e) {
      logger.error(getStackTrace(e));
    }
  }
}

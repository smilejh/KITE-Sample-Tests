package io.cosmosoftware.kite.openvidu;

import io.cosmosoftware.kite.openvidu.checks.AllVideoCheck;
import io.cosmosoftware.kite.openvidu.checks.FirstVideoCheck;
import io.cosmosoftware.kite.openvidu.steps.GoJoinPageStep;
import io.cosmosoftware.kite.openvidu.steps.JoinRoomStep;
import io.cosmosoftware.kite.openvidu.steps.SetUserIdStep;
import io.cosmosoftware.kite.steps.ScreenshotStep;
import org.webrtc.kite.tests.KiteBaseTest;
import org.webrtc.kite.tests.TestRunner;

import static org.webrtc.kite.Utils.getStackTrace;

public class KiteOpenViduTest extends KiteBaseTest {
  public KiteOpenViduTest() {
    super();
  }

  @Override
  protected void populateTestSteps(TestRunner runner) {
    try {
      runner.addStep(new GoJoinPageStep(runner, getRoomManager().getRoomUrl()));
      runner.addStep(new SetUserIdStep(runner, "user" + runner.getId()));
      runner.addStep(new JoinRoomStep(runner));
      if (!fastRampUp()) {
        runner.addStep(new FirstVideoCheck(runner));
        runner.addStep(new AllVideoCheck(runner, getMaxUsersPerRoom()));
      }
      if (takeScreenshotForEachTest()) {
        runner.addStep(new ScreenshotStep(runner));
      }
    } catch (Exception e) {
      logger.error(getStackTrace(e));
    }
  }
}

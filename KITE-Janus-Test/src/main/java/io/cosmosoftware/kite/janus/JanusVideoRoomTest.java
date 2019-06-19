package io.cosmosoftware.kite.janus;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.janus.checks.AllVideoCheck;
import io.cosmosoftware.kite.janus.checks.FirstVideoCheck;
import io.cosmosoftware.kite.janus.pages.JanusPage;
import io.cosmosoftware.kite.janus.steps.GetStatsStep;
import io.cosmosoftware.kite.janus.steps.LeaveDemoStep;
import io.cosmosoftware.kite.janus.steps.StartDemoStep;
import io.cosmosoftware.kite.janus.steps.videoroom.JoinVideoRoomStep;
import io.cosmosoftware.kite.report.Status;
import io.cosmosoftware.kite.steps.ScreenshotStep;
import io.cosmosoftware.kite.steps.WaitForOthersStep;
import io.cosmosoftware.kite.util.TestUtils;
import org.webrtc.kite.tests.KiteBaseTest;
import org.webrtc.kite.tests.TestRunner;

import static org.webrtc.kite.Utils.getStackTrace;

public class JanusVideoRoomTest extends KiteBaseTest {

  protected boolean sfu = false;
  @Override
  public void populateTestSteps(TestRunner runner) {
    try {
      String userName = "user" + TestUtils.idToString(runner.getId());

      final JanusPage janusPage = new JanusPage(runner);
      runner.addStep(new StartDemoStep(runner, this.url));
      //find a way to have no more than 6 user per room with the room manager(flag?) or accept the pop up if there is too many users in the room
      runner.addStep(new JoinVideoRoomStep(runner, userName, janusPage));
      if(janusPage.getRegistrationState()){
        runner.addStep(new WaitForOthersStep(runner, this, runner.getLastStep()));

        runner.addStep(new FirstVideoCheck(runner));
        runner.addStep(new AllVideoCheck(runner, getMaxUsersPerRoom(), janusPage));
        runner.addStep(new WaitForOthersStep(runner, this, runner.getLastStep()));

        if (this.getStats()) {
          runner.addStep(new GetStatsStep(runner, getStatsConfig, sfu, janusPage));
          runner.addStep(new WaitForOthersStep(runner, this, runner.getLastStep()));
        }

        if (this.takeScreenshotForEachTest()) {
          runner.addStep(new ScreenshotStep(runner));
        }

        runner.addStep(new LeaveDemoStep(runner));
      }else{
        throw new KiteTestException("Videoroom is too crowded", Status.BROKEN);
      }

    } catch (Exception e) {
      logger.error(getStackTrace(e));
    }
  }

  @Override
  public void payloadHandling () {
    super.payloadHandling();
    sfu = payload.getBoolean("sfu", false);
  }
}
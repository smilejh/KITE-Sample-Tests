package io.cosmosoftware.kite.janus;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.janus.checks.AllVideoCheck;
import io.cosmosoftware.kite.janus.checks.FirstVideoCheck;
import io.cosmosoftware.kite.janus.pages.JanusPage;
import io.cosmosoftware.kite.janus.steps.GetStatsStep;
import io.cosmosoftware.kite.janus.steps.JoinVideoRoomStep;
import io.cosmosoftware.kite.janus.steps.LeaveDemoStep;
import io.cosmosoftware.kite.janus.steps.StartDemoStep;
import io.cosmosoftware.kite.report.Status;
import io.cosmosoftware.kite.util.TestUtils;
import org.openqa.selenium.WebDriver;
import org.webrtc.kite.steps.ScreenshotStep;
import org.webrtc.kite.steps.WaitForOthersStep;
import org.webrtc.kite.tests.KiteBaseTest;
import org.webrtc.kite.tests.TestRunner;

import static org.webrtc.kite.Utils.getStackTrace;

public class JanusVideoRoomTest extends KiteBaseTest {

  protected boolean sfu = false;
  @Override
  public void populateTestSteps(TestRunner runner) {
    try {
      WebDriver webDriver = runner.getWebDriver();
      String userName = "user" + TestUtils.idToString(runner.getId());

      final JanusPage janusPage = new JanusPage(webDriver, logger);
      runner.addStep(new StartDemoStep(webDriver, this.url));
      //find a way to have no more than 6 user per room with the room manager(flag?) or accept the pop up if there is too many users in the room
      runner.addStep(new JoinVideoRoomStep(webDriver, userName, janusPage));
      if(janusPage.getRegistrationState()){
        runner.addStep(new WaitForOthersStep(webDriver, this, runner.getLastStep()));

        runner.addStep(new FirstVideoCheck(webDriver));
        runner.addStep(new AllVideoCheck(webDriver, getMaxUsersPerRoom(), janusPage));
        runner.addStep(new WaitForOthersStep(webDriver, this, runner.getLastStep()));

        if (this.getStats()) {
          runner.addStep(new GetStatsStep(webDriver, getStatsConfig, sfu, janusPage));
          runner.addStep(new WaitForOthersStep(webDriver, this, runner.getLastStep()));
        }

        if (this.takeScreenshotForEachTest()) {
          runner.addStep(new ScreenshotStep(webDriver));
        }

        runner.addStep(new LeaveDemoStep(webDriver));
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
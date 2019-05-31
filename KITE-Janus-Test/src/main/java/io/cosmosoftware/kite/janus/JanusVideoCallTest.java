package io.cosmosoftware.kite.janus;

import io.cosmosoftware.kite.janus.checks.FirstVideoCheck;
import io.cosmosoftware.kite.janus.checks.ReceiverVideoCheck;
import io.cosmosoftware.kite.janus.steps.*;
import org.openqa.selenium.WebDriver;
import org.webrtc.kite.config.App;
import org.webrtc.kite.config.Browser;
import org.webrtc.kite.config.EndPoint;
import org.webrtc.kite.steps.ScreenshotStep;
import org.webrtc.kite.steps.WaitForOthersStep;
import org.webrtc.kite.tests.KiteBaseTest;
import org.webrtc.kite.tests.TestRunner;

import static org.webrtc.kite.Utils.getStackTrace;

public class JanusVideoCallTest extends KiteBaseTest {
  protected boolean sfu = false;

  @Override
  protected void populateTestSteps(TestRunner runner) {
    try {
      WebDriver webDriver = runner.getWebDriver();
      int runnerId = runner.getId();
      String name = generateTestCaseName();


      runner.addStep(new StartDemoStep(webDriver, this.url));
      runner.addStep(new WaitForOthersStep(webDriver, this, runner.getLastStep()));

      runner.addStep(new RegisterUserToVideoCallStep(webDriver, runnerId, name));
      runner.addStep(new WaitForOthersStep(webDriver, this, runner.getLastStep()));

      runner.addStep(new CallPeerStep(webDriver, runnerId, name));
      runner.addStep(new WaitForOthersStep(webDriver, this, runner.getLastStep()));

      runner.addStep(new JoinVideoCallStep(webDriver, runnerId, name));
      runner.addStep(new WaitForOthersStep(webDriver, this, runner.getLastStep()));

      runner.addStep(new FirstVideoCheck(webDriver));
      runner.addStep(new ReceiverVideoCheck(webDriver));
      if (this.getStats()) {
        runner.addStep(new GetStatsStep(webDriver, getStatsConfig, sfu)); //need to find the name of the remote Peer connections
        runner.addStep(new WaitForOthersStep(webDriver, this, runner.getLastStep()));
      }
      if (this.takeScreenshotForEachTest()) {
        runner.addStep(new ScreenshotStep(webDriver));
      }

      runner.addStep(new LeaveDemoStep(webDriver));
    } catch (Exception e) {
      logger.error(getStackTrace(e));
    }


  }

  @Override
  public void payloadHandling () {
    super.payloadHandling();
    sfu = payload.getBoolean("sfu", false);
  }

  /**
   * used to create unique name for the caller username and callee username
   * if users does not have unique name,
   * @return
   */

  @Override
  protected String generateTestCaseName (){
    StringBuilder name = new StringBuilder();

    for(int index = 0; index < this.endPointList.size(); ++index) {
      EndPoint endPoint = this.endPointList.get(index);
      name.append(endPoint.getPlatform().substring(0, 3));
      if (endPoint instanceof Browser) {
        name.append(((Browser)endPoint).getBrowserName().substring(0, 2));
        name.append(((Browser)endPoint).getVersion());
      } else {
        name.append(((App)endPoint).getDeviceName().substring(0, 2));
      }

    }

    return name.toString();

  }
}

package io.cosmosoftware.kite.janus;

import io.cosmosoftware.kite.janus.checks.FirstVideoCheck;
import io.cosmosoftware.kite.janus.checks.ReceiverVideoCheck;
import io.cosmosoftware.kite.janus.pages.JanusPage;
import io.cosmosoftware.kite.janus.steps.GetStatsStep;
import io.cosmosoftware.kite.janus.steps.LeaveDemoStep;
import io.cosmosoftware.kite.janus.steps.StartDemoStep;
import io.cosmosoftware.kite.janus.steps.videocall.CallPeerStep;
import io.cosmosoftware.kite.janus.steps.videocall.JoinVideoCallStep;
import io.cosmosoftware.kite.janus.steps.videocall.RegisterUserToVideoCallStep;
import io.cosmosoftware.kite.steps.ScreenshotStep;
import io.cosmosoftware.kite.steps.WaitForOthersStep;
import org.webrtc.kite.config.client.App;
import org.webrtc.kite.config.client.Browser;
import org.webrtc.kite.config.client.Client;
import org.webrtc.kite.tests.KiteBaseTest;
import org.webrtc.kite.tests.TestRunner;

import static org.webrtc.kite.Utils.getStackTrace;

public class JanusVideoCallTest extends KiteBaseTest {
  protected boolean sfu = false;

  @Override
  protected void populateTestSteps(TestRunner runner) {
    try {
      int runnerId = runner.getId();
      String name = generateTestCaseName();

      final JanusPage janusPage = new JanusPage(runner);
      runner.addStep(new StartDemoStep(runner, this.url));
      runner.addStep(new WaitForOthersStep(runner, this, runner.getLastStep()));

      runner.addStep(new RegisterUserToVideoCallStep(runner, runnerId, name));
      runner.addStep(new WaitForOthersStep(runner, this, runner.getLastStep()));

      runner.addStep(new CallPeerStep(runner, runnerId, name));
      runner.addStep(new WaitForOthersStep(runner, this, runner.getLastStep()));

      runner.addStep(new JoinVideoCallStep(runner, runnerId, name));
      runner.addStep(new WaitForOthersStep(runner, this, runner.getLastStep()));

      runner.addStep(new FirstVideoCheck(runner));
      runner.addStep(new ReceiverVideoCheck(runner));
      if (this.getStats()) {
        runner.addStep(new GetStatsStep(runner, getStatsConfig, sfu, janusPage)); //need to find the name of the remote Peer connections
        runner.addStep(new WaitForOthersStep(runner, this, runner.getLastStep()));
      }
      if (this.takeScreenshotForEachTest()) {
        runner.addStep(new ScreenshotStep(runner));
      }

      runner.addStep(new LeaveDemoStep(runner));
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

    for(int index = 0; index < this.tuple.size(); ++index) {
      Client client = this.tuple.get(index);
      name.append(client.retrievePlatform().name(), 0, 3);
      if (client instanceof Browser) {
        name.append(((Browser)client).getBrowserName(), 0, 2);
        name.append(((Browser)client).getVersion());
      } else {
        name.append(((App)client).retrieveDeviceName(), 0, 2);
      }

    }

    return name.toString();

  }
}

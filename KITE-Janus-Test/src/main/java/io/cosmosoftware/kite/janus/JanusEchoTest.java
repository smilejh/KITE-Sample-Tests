package io.cosmosoftware.kite.janus;


import io.cosmosoftware.kite.janus.checks.FirstVideoCheck;
import io.cosmosoftware.kite.janus.checks.ReceiverVideoCheck;
import io.cosmosoftware.kite.janus.pages.JanusPage;
import io.cosmosoftware.kite.janus.steps.GetStatsStep;
import io.cosmosoftware.kite.janus.steps.StartDemoStep;
import io.cosmosoftware.kite.steps.ScreenshotStep;
import org.webrtc.kite.tests.KiteBaseTest;
import org.webrtc.kite.tests.TestRunner;

import static org.webrtc.kite.Utils.getStackTrace;

public class JanusEchoTest extends KiteBaseTest {

  protected boolean sfu = false;


  @Override
  public void populateTestSteps(TestRunner runner) {
    try {
      
      runner.addStep(new StartDemoStep(runner, this.url));
      runner.addStep(new FirstVideoCheck(runner));
      runner.addStep(new ScreenshotStep(runner));
      runner.addStep(new ReceiverVideoCheck(runner));
      final JanusPage janusPage = new JanusPage(runner);
      if (this.getStats()) {
        runner.addStep(new GetStatsStep( runner, getStatsConfig, sfu, janusPage));

      }
      if (this.takeScreenshotForEachTest()) {
        runner.addStep(new ScreenshotStep(runner));
      }

      } catch(Exception e){
        logger.error(getStackTrace(e));
      }
  }


  @Override
  public void payloadHandling () {
    super.payloadHandling();
    sfu = payload.getBoolean("sfu", false);
  }
}


package io.cosmosoftware.kite.janus;


import io.cosmosoftware.kite.janus.checks.StreamingVideoCheck;
import io.cosmosoftware.kite.janus.pages.JanusPage;
import io.cosmosoftware.kite.janus.steps.GetStatsStep;
import io.cosmosoftware.kite.janus.steps.LeaveDemoStep;
import io.cosmosoftware.kite.janus.steps.StartDemoStep;
import io.cosmosoftware.kite.janus.steps.streaming.JoinStreamingStep;
import io.cosmosoftware.kite.steps.ScreenshotStep;
import org.webrtc.kite.tests.KiteBaseTest;
import org.webrtc.kite.tests.TestRunner;

import static org.webrtc.kite.Utils.getStackTrace;

public class JanusStreamingTest extends KiteBaseTest {

  protected boolean sfu = false;

  @Override
  protected void populateTestSteps(TestRunner runner) {
    try {
      final JanusPage janusPage = new JanusPage(runner);
      runner.addStep(new StartDemoStep(runner, this.url));
      runner.addStep(new JoinStreamingStep(runner, "videoLive"));
      runner.addStep(new StreamingVideoCheck(runner));

      if (this.getStats()) {

        runner.addStep(new GetStatsStep(runner, getStatsConfig, sfu, janusPage));
      }
      if (this.takeScreenshotForEachTest()) {
        runner.addStep(new ScreenshotStep(runner));
      }

      //see how the audioCheck can be adapted for the check of a live audio streaming

      runner.addStep(new LeaveDemoStep(runner));

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

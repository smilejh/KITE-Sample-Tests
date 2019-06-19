package io.cosmosoftware.kite.janus;


import io.cosmosoftware.kite.janus.checks.StreamingVideoCheck;
import io.cosmosoftware.kite.janus.pages.JanusPage;
import io.cosmosoftware.kite.janus.steps.GetStatsStep;
import io.cosmosoftware.kite.janus.steps.JoinStreamingStep;
import io.cosmosoftware.kite.janus.steps.LeaveDemoStep;
import io.cosmosoftware.kite.janus.steps.StartDemoStep;
import io.cosmosoftware.kite.janus.steps.streaming.JoinStreamingStep;
import org.openqa.selenium.WebDriver;
import org.webrtc.kite.steps.ScreenshotStep;
import org.webrtc.kite.tests.KiteBaseTest;
import org.webrtc.kite.tests.TestRunner;

import static org.webrtc.kite.Utils.getStackTrace;

public class JanusStreamingTest extends KiteBaseTest {

  protected boolean sfu = false;

  @Override
  protected void populateTestSteps(TestRunner runner) {
    try {
      WebDriver webDriver = runner.getWebDriver();

      final JanusPage janusPage = new JanusPage(webDriver, logger);
      runner.addStep(new StartDemoStep(webDriver, this.url));
      runner.addStep(new JoinStreamingStep(webDriver, "videoLive"));
      runner.addStep(new StreamingVideoCheck(webDriver));

      if (this.getStats()) {

        runner.addStep(new GetStatsStep(webDriver, getStatsConfig, sfu, janusPage));
      }
      if (this.takeScreenshotForEachTest()) {
        runner.addStep(new ScreenshotStep(webDriver));
      }

      //see how the audioCheck can be adapted for the check of a live audio streaming

      runner.addStep(new LeaveDemoStep(webDriver));

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

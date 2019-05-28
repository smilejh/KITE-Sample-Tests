package io.cosmosoftware.kite.janus;


import io.cosmosoftware.kite.janus.checks.AudioCheck;
import io.cosmosoftware.kite.janus.checks.FirstVideoCheck;
import io.cosmosoftware.kite.janus.steps.GetStatsStep;
import io.cosmosoftware.kite.janus.steps.JoinStreamingStep;
import io.cosmosoftware.kite.janus.steps.StartDemoStep;
import org.openqa.selenium.WebDriver;
import org.webrtc.kite.steps.ScreenshotStep;
import org.webrtc.kite.tests.KiteBaseTest;
import org.webrtc.kite.tests.TestRunner;

import static org.webrtc.kite.Utils.getStackTrace;

public class JanusStreamingTest extends KiteBaseTest {
  @Override
  protected void populateTestSteps(TestRunner runner) {
    try {
      WebDriver webDriver = runner.getWebDriver();
      runner.addStep(new StartDemoStep(webDriver, this.url));
      runner.addStep(new JoinStreamingStep(webDriver, "videoLive"));
      runner.addStep(new FirstVideoCheck(webDriver));

      //getStats does not work for now because I did not find a name in the website source code for the PeerConnection that match the other test
      if (this.getStats()) {
        runner.addStep(new GetStatsStep(webDriver, getStatsConfig));
      }
      if (this.takeScreenshotForEachTest()) {
        runner.addStep(new ScreenshotStep(webDriver));
      }

      //see how the audioCheck can be adapted for the check of a live audio streaming
      runner.addStep(new JoinStreamingStep(webDriver, "audioLive"));
//      runner.addStep(new AudioCheck(webDriver, ));


      //getStats does not work for now because I did not find a name in the website source code for the PeerConnection that match the other test
//      if (this.getStats()) {
//        runner.addStep(new GetStatsStep(webDriver, getStatsConfig));
//      }
//      if (this.takeScreenshotForEachTest()) {
//        runner.addStep(new ScreenshotStep(webDriver));
//      }
    } catch(Exception e){
      logger.error(getStackTrace(e));
    }


  }
}

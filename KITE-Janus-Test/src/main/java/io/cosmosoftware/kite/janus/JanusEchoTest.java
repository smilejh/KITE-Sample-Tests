package io.cosmosoftware.kite.janus;


import io.cosmosoftware.kite.janus.checks.*;
import io.cosmosoftware.kite.janus.pages.JanusPage;
import io.cosmosoftware.kite.janus.steps.*;
import org.openqa.selenium.WebDriver;
import org.webrtc.kite.steps.ScreenshotStep;
import org.webrtc.kite.tests.KiteBaseTest;
import org.webrtc.kite.tests.TestRunner;

import static org.webrtc.kite.Utils.getStackTrace;

public class JanusEchoTest extends KiteBaseTest {

  protected boolean sfu = false;


  @Override
  public void populateTestSteps(TestRunner runner) {
    try {

      WebDriver webDriver = runner.getWebDriver();
      runner.addStep(new StartDemoStep(webDriver, this.url));
      runner.addStep(new FirstVideoCheck(webDriver));
      runner.addStep(new ScreenshotStep(webDriver));
      runner.addStep(new ReceiverVideoCheck(webDriver));
      final JanusPage janusPage = new JanusPage(webDriver, logger);
      if (this.getStats()) {
        runner.addStep(new GetStatsStep( webDriver, getStatsConfig, sfu, janusPage));

      }
      if (this.takeScreenshotForEachTest()) {
        runner.addStep(new ScreenshotStep(webDriver));
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


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

  private final String[] rids = {"a", "b", "c"};
  private final int[] tids = {0, 1, 2};
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
      //the next if part can be removed
      if (url.contains("simulcast=true")){
        for (String rid : rids) {
          for (int tid : tids) {


            runner.addStep(new SelectProfileStep(webDriver, janusPage, rid, tid));
            runner.addStep(new GaugesCheck(webDriver, janusPage, rid, tid));
          }
        }
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


package io.cosmosoftware.kite.janus;


import io.cosmosoftware.kite.janus.checks.FirstVideoCheck;
import io.cosmosoftware.kite.janus.checks.GaugesCheck;
import io.cosmosoftware.kite.janus.pages.JanusPage;
import io.cosmosoftware.kite.janus.steps.GetStatsStep;
import io.cosmosoftware.kite.janus.steps.SelectProfileStep;
import io.cosmosoftware.kite.janus.steps.StartDemoStep;
import io.cosmosoftware.kite.janus.steps.ScreenshotStep;
import io.cosmosoftware.kite.simulcast.checks.ReceiverVideoCheck;
import org.openqa.selenium.WebDriver;
import org.webrtc.kite.tests.KiteBaseTest;
import org.webrtc.kite.tests.TestRunner;

import static org.webrtc.kite.Utils.getStackTrace;

public class JanusEchoTest extends KiteBaseTest {

  private final String demoName = "echotest";

  private final String[] rids = {"a", "b", "c"};
  private final int[] tids = {0, 1, 2};


  @Override
  public void populateTestSteps(TestRunner runner) {
    try {

      WebDriver webDriver = runner.getWebDriver();
      runner.addStep(new StartDemoStep(webDriver, this.url));
      runner.addStep(new FirstVideoCheck(webDriver));
      runner.addStep(new ScreenshotStep(webDriver));
      runner.addStep(new ReceiverVideoCheck(webDriver));

      if (this.getStats()) {
        runner.addStep(
            new GetStatsStep(
                  webDriver,
                  getMaxUsersPerRoom(),
                  getStatsCollectionTime(),
                  getStatsCollectionInterval(),
                  getSelectedStats(),
                 demoName)
            );

      }
      if (this.takeScreenshotForEachTest()) {
        runner.addStep(new ScreenshotStep(webDriver));
      }
      //the next if part can be removed
      if (url.contains("simulcast=true")){
        for (String rid : rids) {
          for (int tid : tids) {

            final JanusPage janusPage = new JanusPage(webDriver, logger);
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
  }
}


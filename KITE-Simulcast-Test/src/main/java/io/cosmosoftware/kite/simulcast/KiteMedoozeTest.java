package io.cosmosoftware.kite.simulcast;

import io.cosmosoftware.kite.simulcast.checks.GaugesCheck;
import io.cosmosoftware.kite.simulcast.checks.ReceiverVideoCheck;
import io.cosmosoftware.kite.simulcast.checks.SenderVideoCheck;
import io.cosmosoftware.kite.simulcast.pages.MedoozeLoopbackPage;
import io.cosmosoftware.kite.simulcast.steps.GetStatsStep;
import io.cosmosoftware.kite.simulcast.steps.LoadPageStep;
import io.cosmosoftware.kite.simulcast.steps.ScreenshotStep;
import io.cosmosoftware.kite.simulcast.steps.SelectProfileStep;
import org.webrtc.kite.tests.KiteBaseTest;
import org.webrtc.kite.tests.TestRunner;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import javax.json.JsonObject;

public class KiteMedoozeTest extends KiteBaseTest {



  private final String[] rids = {"a", "b", "c"};
  private final int[] tids = {0, 1, 2};
    
  @Override
  public void populateTestSteps(TestRunner runner) {
    WebDriver webDriver = runner.getWebDriver();
    runner.addStep(new LoadPageStep(webDriver, this.url));
    if (!this.fastRampUp()) {
      MedoozeLoopbackPage page = new MedoozeLoopbackPage(webDriver, logger);
      runner.addStep(new SenderVideoCheck(webDriver, page));
      runner.addStep(new ReceiverVideoCheck(webDriver, page));
      if (this.getStats()) {
        runner.addStep(
            new GetStatsStep(
                webDriver,
                getStatsCollectionTime(),
                getStatsCollectionInterval(),
                getSelectedStats(),
              "pc"));
      }
      if (this.takeScreenshotForEachTest()) {
        runner.addStep(new ScreenshotStep(webDriver));
      }
      for (String rid : rids) {
        for (int tid : tids) {
          runner.addStep(new SelectProfileStep(webDriver, page, rid, tid));
          runner.addStep(new GaugesCheck(webDriver, page, rid, tid));
        }
      }
    }
  }
}

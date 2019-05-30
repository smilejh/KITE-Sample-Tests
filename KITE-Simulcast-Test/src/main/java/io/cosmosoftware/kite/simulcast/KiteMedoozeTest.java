package io.cosmosoftware.kite.simulcast;

import io.cosmosoftware.kite.simulcast.checks.*;
import io.cosmosoftware.kite.simulcast.pages.*;
import io.cosmosoftware.kite.simulcast.steps.*;
import org.openqa.selenium.WebDriver;
import org.webrtc.kite.steps.ScreenshotStep;
import org.webrtc.kite.tests.KiteBaseTest;
import org.webrtc.kite.tests.TestRunner;

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
        runner.addStep(new GetStatsStep(webDriver, getStatsConfig));
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

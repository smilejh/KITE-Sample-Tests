package io.cosmosoftware.kite.simulcast;

import io.cosmosoftware.kite.simulcast.checks.GaugesCheck;
import io.cosmosoftware.kite.simulcast.checks.ReceiverVideoCheck;
import io.cosmosoftware.kite.simulcast.checks.SenderVideoCheck;
import io.cosmosoftware.kite.simulcast.pages.MedoozeLoopbackPage;
import io.cosmosoftware.kite.simulcast.steps.GetStatsStep;
import io.cosmosoftware.kite.simulcast.steps.LoadPageStep;
import io.cosmosoftware.kite.simulcast.steps.SelectProfileStep;
import io.cosmosoftware.kite.steps.ScreenshotStep;
import org.webrtc.kite.tests.KiteBaseTest;
import org.webrtc.kite.tests.TestRunner;

public class KiteMedoozeTest extends KiteBaseTest {

  private final String[] rids = {"a", "b", "c"};
  private final int[] tids = {0, 1, 2};
    
  @Override
  public void populateTestSteps(TestRunner runner) {
    runner.addStep(new LoadPageStep(runner, this.url));
    if (!this.fastRampUp()) {
      MedoozeLoopbackPage page = new MedoozeLoopbackPage(runner);
      runner.addStep(new SenderVideoCheck(runner, page));
      runner.addStep(new ReceiverVideoCheck(runner, page));
      if (this.getStats()) {
        runner.addStep(new GetStatsStep(runner, getStatsConfig));
      }
      if (this.takeScreenshotForEachTest()) {
        runner.addStep(new ScreenshotStep(runner));
      }
      for (String rid : rids) {
        for (int tid : tids) {
          runner.addStep(new SelectProfileStep(runner, page, rid, tid));
          runner.addStep(new GaugesCheck(runner, page, rid, tid));
        }
      }
    }
  }
}

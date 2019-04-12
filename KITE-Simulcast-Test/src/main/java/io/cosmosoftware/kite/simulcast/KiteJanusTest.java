package io.cosmosoftware.kite.simulcast;

import io.cosmosoftware.kite.simulcast.checks.BandwidthCheck;
import io.cosmosoftware.kite.simulcast.checks.GaugesCheck;
import io.cosmosoftware.kite.simulcast.checks.ReceiverVideoCheck;
import io.cosmosoftware.kite.simulcast.checks.SenderVideoCheck;
import io.cosmosoftware.kite.simulcast.pages.JanusLoopbackPage;
import io.cosmosoftware.kite.simulcast.steps.GetStatsStep;
import io.cosmosoftware.kite.simulcast.steps.LoadPageStep;
import io.cosmosoftware.kite.simulcast.steps.ScreenshotStep;
import io.cosmosoftware.kite.simulcast.steps.SelectProfileStep;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.webrtc.kite.tests.KiteBaseTest;
import org.webrtc.kite.tests.TestRunner;

import javax.json.JsonObject;

public class KiteJanusTest extends KiteBaseTest {


  private static final Logger logger = Logger.getLogger(KiteJanusTest.class.getName());

  private int loadReachTime = 0;
  private int bandwidthCheckDuration = 0;
  private boolean checkSimulcast = true;

  private final String[] rids = {"a", "b", "c"};
  private final int[] tids = {0, 1, 2};

  @Override
  protected void payloadHandling() {
    super.payloadHandling();
    JsonObject jsonPayload = (JsonObject) this.payload;
    if (jsonPayload != null) {
      checkSimulcast = jsonPayload.getBoolean("checkSimulcast", checkSimulcast);
      loadReachTime = jsonPayload.getInt("loadReachTime", loadReachTime);
      bandwidthCheckDuration = jsonPayload.getInt("bandwidthCheckDuration", loadReachTime);
      setExpectedTestDuration(Math.max(getExpectedTestDuration(), (loadReachTime + 300)/60));
    }
  }

  @Override
  public void populateTestSteps(TestRunner runner) {
    WebDriver webDriver = runner.getWebDriver();
    runner.addStep(new LoadPageStep(webDriver, this.url));
    if (!this.fastRampUp()) {
      JanusLoopbackPage page = new JanusLoopbackPage(webDriver);
      runner.addStep(new SenderVideoCheck(webDriver, page));
      runner.addStep(new ReceiverVideoCheck(webDriver, page));
      if (this.getStats()) {
        runner.addStep(
            new GetStatsStep(
                webDriver,
                getStatsCollectionTime(),
                getStatsCollectionInterval(),
                getSelectedStats(),
              "echotest.webrtcStuff.pc"));
      }
      if (this.takeScreenshotForEachTest()) {
        runner.addStep(new ScreenshotStep(webDriver));
      }
      if (bandwidthCheckDuration > 0) {
        runner.addStep(new BandwidthCheck(webDriver, page, 1000000, bandwidthCheckDuration));
      }
      if (checkSimulcast) {
        for (String rid : rids) {
          if (this.url.contains("h264")) {
            runner.addStep(new SelectProfileStep(webDriver, page, rid, -1));
            runner.addStep(new GaugesCheck(webDriver, page, rid, -1));
          } else {
            for (int tid : tids) {
              runner.addStep(new SelectProfileStep(webDriver, page, rid, tid));
              runner.addStep(new GaugesCheck(webDriver, page, rid, tid));
            }
          }
        }
      }
    }
  }
}

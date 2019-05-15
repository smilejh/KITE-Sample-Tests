package io.cosmosoftware.kite.jitsi;

import io.cosmosoftware.kite.jitsi.checks.AllVideoCheck;
import io.cosmosoftware.kite.jitsi.checks.FirstVideoCheck;
import io.cosmosoftware.kite.jitsi.steps.GetStatsStep;
import io.cosmosoftware.kite.jitsi.steps.JoinRoomStep;
import io.cosmosoftware.kite.jitsi.steps.ScreenshotStep;
import io.cosmosoftware.kite.jitsi.steps.LoadGetStatsStep;
import org.openqa.selenium.WebDriver;
import org.webrtc.kite.tests.KiteBaseTest;
import org.webrtc.kite.tests.TestRunner;

import static org.webrtc.kite.Utils.getStackTrace;


import javax.json.JsonObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class KiteJitsiTest extends KiteBaseTest {
  final Random rand = new Random(System.currentTimeMillis());
  private JsonObject getStatsSdk;
  private String testName =  null;
  private String testId = "\"" + this.name + "_" + new SimpleDateFormat("yyyyMMdd_hhmmss").format(new Date()) + "\"";
  private String logstashUrl =  null;
  private String sfu = "Jitsi";
  private int statsPublishingInterval = 30000;
  private String pathToGetStatsSdk;

  @Override
  protected void payloadHandling() {
    super.payloadHandling();
    if (this.payload != null) {

      getStatsSdk = this.payload.getJsonObject("getStatsSdk");
      testName = this.name;
      testId = getStatsSdk.getString("testId");
      logstashUrl = getStatsSdk.getString("logstashUrl");
      sfu = getStatsSdk.getString("sfu");
      statsPublishingInterval = getStatsSdk.getInt("statsPublishingInterval", statsPublishingInterval);
      pathToGetStatsSdk = this.payload.getString("pathToGetStatsSdk", pathToGetStatsSdk);

    }
  }

  @Override
  protected void populateTestSteps(TestRunner runner) {
    try {
      WebDriver webDriver = runner.getWebDriver();
      runner.addStep(new JoinRoomStep(webDriver, getRoomManager().getRoomUrl()));
      runner.addStep(new FirstVideoCheck(webDriver));
      runner.addStep(new AllVideoCheck(webDriver));
      if (this.getStats()) {
        runner.addStep(
                new GetStatsStep(
                        webDriver,
                        getStatsCollectionTime(),
                        getStatsCollectionInterval(),
                        getSelectedStats()));
      }
      if (this.sfu != null) {
        runner.addStep(new LoadGetStatsStep(runner.getWebDriver(), testName, testId, logstashUrl, sfu, statsPublishingInterval, pathToGetStatsSdk));
      }
      if (this.takeScreenshotForEachTest()) {
        runner.addStep(new ScreenshotStep(webDriver));
      }


    } catch (Exception e) {
      logger.error(getStackTrace(e));
    }
  }
}

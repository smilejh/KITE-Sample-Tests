package io.cosmosoftware.kite.jitsi;

import io.cosmosoftware.kite.jitsi.checks.AllVideoCheck;
import io.cosmosoftware.kite.jitsi.steps.GetStatsStep;
import io.cosmosoftware.kite.jitsi.steps.JoinRoomStep;
import io.cosmosoftware.kite.jitsi.steps.LoadGetStatsStep;
import io.cosmosoftware.kite.jitsi.steps.ScreenshotStep;
import org.webrtc.kite.tests.KiteBaseTest;
import org.webrtc.kite.tests.TestRunner;

import javax.json.JsonObject;
import javax.json.JsonValue;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class KiteJitsiTest extends KiteBaseTest {
  public static String url = "https://meet.jit.si";
  final Random rand = new Random(System.currentTimeMillis());
  final String roomId = String.valueOf(Math.abs(rand.nextLong()));
  private JsonObject getStatsSdk;
  private String testName =  null;
  private String testId = "\"" + this.name + "_" + new SimpleDateFormat("yyyyMMdd_hhmmss").format(new Date()) + "\"";
  private JsonValue logstashUrl =  null;
  private String sfu = "Jitsi";
  private JsonValue statsPublishingInterval;
  private String pathToGetStatsSdk;

  @Override
  protected void payloadHandling() {
    super.payloadHandling();
    if (this.payload != null) {

      getStatsSdk = this.payload.getJsonObject("getStatsSdk");
      testName = this.name;
      testId = getStatsSdk.get("testId").toString();
      logstashUrl = getStatsSdk.get("logstashUrl");
      sfu = getStatsSdk.get("sfu").toString();
      statsPublishingInterval = getStatsSdk.get("statsPublishingInterval");
      pathToGetStatsSdk = this.payload.getString("pathToGetStatsSdk", pathToGetStatsSdk);

    }
  }

  @Override
  protected void populateTestSteps(TestRunner runner) {
    JoinRoomStep joinRoomStep = new JoinRoomStep(runner.getWebDriver());
    joinRoomStep.setRoomId(roomId);
    runner.addStep(joinRoomStep);
    runner.addStep(new AllVideoCheck(runner.getWebDriver()));
    if (this.getStats()) {
      runner.addStep(
          new GetStatsStep(
              runner.getWebDriver(),
              getStatsCollectionTime(),
              getStatsCollectionInterval(),
              getSelectedStats()));
    }
    if (this.sfu != null) {
      runner.addStep(new LoadGetStatsStep(runner.getWebDriver(), testName, testId, logstashUrl, sfu, statsPublishingInterval, pathToGetStatsSdk));
    }
    runner.addStep(new ScreenshotStep(runner.getWebDriver()));
  }
}

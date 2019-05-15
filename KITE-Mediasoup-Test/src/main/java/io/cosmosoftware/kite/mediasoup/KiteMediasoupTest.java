package io.cosmosoftware.kite.mediasoup;

import io.cosmosoftware.kite.mediasoup.checks.AllVideoCheck;
import io.cosmosoftware.kite.mediasoup.checks.FirstVideoCheck;
import io.cosmosoftware.kite.mediasoup.steps.LoadGetStatsStep;
import io.cosmosoftware.kite.mediasoup.steps.GetStatsStep;
import io.cosmosoftware.kite.mediasoup.steps.JoinVideoCallStep;
import io.cosmosoftware.kite.mediasoup.steps.ScreenshotStep;
import io.cosmosoftware.kite.mediasoup.steps.StayInMeetingStep;
import org.webrtc.kite.tests.KiteBaseTest;
import org.webrtc.kite.tests.TestRunner;
import org.openqa.selenium.WebDriver;

import javax.json.JsonArray;
import javax.json.JsonObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.webrtc.kite.Utils.getStackTrace;


public class KiteMediasoupTest extends KiteBaseTest {

  private int loadReachTime = 0;
  private JsonObject getStatsSdk;
  private String testName =  null;
  private String testId = "\"" + this.name + "_" + new SimpleDateFormat("yyyyMMdd_hhmmss").format(new Date()) + "\"";
  private String logstashUrl =  null;
  private String sfu = "Mediasoup";
  private int statsPublishingInterval = 30000;
  private String pathToGetStatsSdk;

  
  @Override
  protected void payloadHandling() {
    super.payloadHandling();
    JsonObject jsonPayload = (JsonObject) this.payload;
    String[] rooms = null;
    if (jsonPayload != null) {
      getStatsSdk = this.payload.getJsonObject("getStatsSdk");
      testName = this.name;
      testId = getStatsSdk.getString("testId");
      logstashUrl = getStatsSdk.getString("logstashUrl");
      sfu = getStatsSdk.getString("sfu");
      statsPublishingInterval = getStatsSdk.getInt("statsPublishingInterval", statsPublishingInterval);
      pathToGetStatsSdk = this.payload.getString("pathToGetStatsSdk", pathToGetStatsSdk);

      loadReachTime = jsonPayload.getInt("loadReachTime", loadReachTime);
      setExpectedTestDuration(Math.max(getExpectedTestDuration(), (loadReachTime + 300)/60));
      JsonArray jsonArray = jsonPayload.getJsonArray("rooms");
      rooms = new String[jsonArray.size()];
      for (int i = 0; i < jsonArray.size(); i++) {
        rooms[i] = jsonArray.getString(i);
      }
    }
    if (rooms != null) {
      getRoomManager().setRoomNames(rooms);
    }
  }
  
  @Override
  public void populateTestSteps(TestRunner runner) {
    try {
      WebDriver webDriver = runner.getWebDriver();
      runner.addStep(new JoinVideoCallStep(webDriver, getRoomManager().getRoomUrl()));
      if (!this.fastRampUp()) {
        runner.addStep(new FirstVideoCheck(webDriver));
        runner.addStep(new AllVideoCheck(webDriver, getMaxUsersPerRoom()));
        if (this.getStats()) {
          runner.addStep(
                  new GetStatsStep(
                          webDriver,
                          getMaxUsersPerRoom(),
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
        if (this.loadReachTime > 0) {
          runner.addStep(new StayInMeetingStep(webDriver, loadReachTime));
        }
      }
    } catch (Exception e) {
      logger.error(getStackTrace(e));
    }
  }
}

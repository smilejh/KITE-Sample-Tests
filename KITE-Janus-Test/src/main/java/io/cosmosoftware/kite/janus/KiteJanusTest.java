package io.cosmosoftware.kite.janus;

import io.cosmosoftware.kite.janus.checks.AllVideoCheck;
import io.cosmosoftware.kite.janus.checks.AudioCheck;
import io.cosmosoftware.kite.janus.checks.FirstVideoCheck;
import io.cosmosoftware.kite.janus.steps.*;
import io.cosmosoftware.kite.util.TestUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.webrtc.kite.tests.KiteBaseTest;
import org.webrtc.kite.tests.TestRunner;

import javax.json.JsonArray;

import static org.webrtc.kite.Utils.getStackTrace;


public class KiteJanusTest extends KiteBaseTest {
  

  private int loadReachTime = 0;
  private String audioScoreWorkingDirectory = null;
  private String audioScoreTool = null;
  private String audioDuration = null;
  private String testName;
  private String testId;

  @Override
  protected void payloadHandling() {
    super.payloadHandling();
    String[] rooms = null;
    if (this.payload != null) {
      testName = this.payload.getString("testName", testName);
      testId = this.payload.getString("testId", testId);
      loadReachTime = this.payload.getInt("loadReachTime", loadReachTime);
      setExpectedTestDuration(Math.max(getExpectedTestDuration(), (loadReachTime + 300)/60));
      JsonArray jsonArray = this.payload.getJsonArray("rooms");
      rooms = new String[jsonArray.size()];
      for (int i = 0; i < jsonArray.size(); i++) {
        rooms[i] = jsonArray.getString(i);
      }
      audioScoreWorkingDirectory= this.payload.getString("audioScoreWorkingDirectory", audioScoreWorkingDirectory);
      audioScoreTool= this.payload.getString("audioScoreTool", audioScoreTool);
      audioDuration= this.payload.getString("audioDuration", audioDuration);
    }
    if (rooms != null) {
      getRoomManager().setRoomNames(rooms);
    }
  }

  @Override
  public void populateTestSteps(TestRunner runner) {
    try {
      WebDriver webDriver = runner.getWebDriver();
      String roomUrl = getRoomManager().getRoomUrl()  + "&username=user" + TestUtils.idToString(runner.getId());
      runner.addStep(new JoinVideoCallStep(webDriver, roomUrl));
      if (!this.fastRampUp()) {
        runner.addStep(new FirstVideoCheck(webDriver));
        runner.addStep(new AllVideoCheck(webDriver, getMaxUsersPerRoom()));
        if (this.getStats()) {
          runner.addStep(new LoadGetStatsStep(webDriver, testName, testId, "C:\\Users\\Karen\\Documents\\KITE-getstats-sdk.js"));
          runner.addStep(
              new GetStatsStep(
                  webDriver,
                  getMaxUsersPerRoom(),
                  getStatsCollectionTime(),
                  getStatsCollectionInterval(),
                  getSelectedStats()));
        }
        if (this.takeScreenshotForEachTest()) {
          runner.addStep(new ScreenshotStep(webDriver));
        }
        if (this.loadReachTime > 0) {
          runner.addStep(new StayInMeetingStep(webDriver, loadReachTime));
        }
        if (this.getNWInstConfig() != null) {
          runner.addStep(new NWInstrumentationStep(webDriver, getNWInstConfig()));
          runner.addStep(new GetStatsStep(webDriver, getMaxUsersPerRoom(),
            getStatsCollectionTime(), getStatsCollectionInterval(), getSelectedStats()));
          runner.addStep(new NWInstCleanupStep(webDriver, getNWInstConfig()));
        }
        if (this.audioScoreWorkingDirectory != null) {
          if (runner.getId() == 0) {
            runner.addStep(new UnpublishStep(webDriver));
          } else {
            runner.addStep(new AudioCheck(webDriver, this.sessionData.get(webDriver), audioScoreWorkingDirectory,
              audioScoreTool, audioDuration));
          }
        }
      }
    } catch (Exception e) {
      logger.error(getStackTrace(e));
    }
  }
}

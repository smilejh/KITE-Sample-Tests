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
import javax.json.JsonObject;

import static org.webrtc.kite.Utils.getStackTrace;


public class KiteJanusTest extends KiteBaseTest {


  private static final Logger logger = Logger.getLogger(KiteJanusTest.class.getName());

  private int loadReachTime = 0;

  private static int driverNumber = 0;

  @Override
  protected void payloadHandling() {
    super.payloadHandling();
    JsonObject jsonPayload = (JsonObject) this.payload;
    String[] rooms = null;
    if (jsonPayload != null) {
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
      int id = 1;
      String roomUrl = getRoomManager().getRoomUrl()  + "&username=user" + TestUtils.idToString(id++);
      runner.addStep(new JoinVideoCallStep(webDriver, roomUrl));
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
      }
      if (runner.getId() == 0) {
        runner.addStep(new UnpublishStep(webDriver));
      } else {
        AudioCheck auCheck = new AudioCheck(webDriver, this.sessionData.get(webDriver));
        auCheck.setScoreDirectory(this.payload.getString("audioScoreWorkingDirectory"));
        auCheck.setScoreCommand(this.payload.getString("audioScoreTool"));
        auCheck.setAudioDuration(this.payload.getString("audioDuration"));
        runner.addStep(auCheck);
      }
    } catch (Exception e) {
      logger.error(getStackTrace(e));
    }
  }
}

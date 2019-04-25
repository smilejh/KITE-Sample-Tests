package io.cosmosoftware.kite.janus;

import io.cosmosoftware.kite.axel.Instrumentation;
import io.cosmosoftware.kite.janus.checks.AllVideoCheck;
import io.cosmosoftware.kite.janus.checks.AudioCheck;
import io.cosmosoftware.kite.janus.checks.FirstVideoCheck;
import io.cosmosoftware.kite.janus.steps.*;
import io.cosmosoftware.kite.util.TestUtils;
import org.openqa.selenium.WebDriver;
import org.webrtc.kite.tests.KiteBaseTest;
import org.webrtc.kite.tests.TestRunner;
import org.webrtc.kite.tests.WaitForOthersStep;

import javax.json.JsonArray;
import java.util.ArrayList;

import static org.webrtc.kite.Utils.getStackTrace;


public class KiteJanusTest extends KiteBaseTest {
  

  private int loadReachTime = 0;
  private String audioScoreWorkingDirectory = null;
  private String audioScoreTool = null;
  private String audioDuration = null;
  private ArrayList<Scenario> scenarioArrayList = new ArrayList<>();

  @Override
  protected void payloadHandling() {
    super.payloadHandling();
    String[] rooms = null;
    if (this.payload != null) {
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
      JsonArray jsonArray2 = this.payload.getJsonArray("scenarios");
      for(int i = 0; i < jsonArray2.size(); ++i) {
        this.scenarioArrayList.add(new Scenario(jsonArray2.getJsonObject(i), logger));
      }
    }
    if (rooms != null) {
      getRoomManager().setRoomNames(rooms);
    }
  }

  @Override
  public void populateTestSteps(TestRunner runner) {
    try {
      Instrumentation nwInstrumentation = getInstrumentation();
      WebDriver webDriver = runner.getWebDriver();
      String roomUrl = getRoomManager().getRoomUrl()  + "&username=user" + TestUtils.idToString(runner.getId());
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

        //nwInstrumentation.sendCommand(scenarios[0].gateway, scenarios[0].command);
        //step wait for scenarios[0].duration seconds
        //step getstats
        //step screenshot
        runner.addStep(new WaitForOthersStep(webDriver, this, runner.getLastStep()));

        for (Scenario scenario : this.scenarioArrayList ) {
          runner.addStep(new NWInstrumentationStep(webDriver, scenario, nwInstrumentation, runner.getId()));
          runner.addStep(new WaitForOthersStep(webDriver, this, runner.getLastStep()));
          runner.addStep(new GetStatsStep(webDriver, getMaxUsersPerRoom(),
                  getStatsCollectionTime(), getStatsCollectionInterval(), getSelectedStats()));
          runner.addStep(new ScreenshotStep(webDriver));
          runner.addStep(new WaitForOthersStep(webDriver, this, runner.getLastStep()));
          runner.addStep(new NWInstCleanupStep(webDriver, scenario, nwInstrumentation, runner.getId()));
          runner.addStep(new WaitForOthersStep(webDriver, this, runner.getLastStep()));
        }

        /*
        if (this.getNWInstConfig() != null) {
          runner.addStep(new NWInstrumentationStep(webDriver, getNWInstConfig()));
          runner.addStep(new GetStatsStep(webDriver, getMaxUsersPerRoom(),
            getStatsCollectionTime(), getStatsCollectionInterval(), getSelectedStats()));
          runner.addStep(new NWInstCleanupStep(webDriver, getNWInstConfig()));
        }
        */
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

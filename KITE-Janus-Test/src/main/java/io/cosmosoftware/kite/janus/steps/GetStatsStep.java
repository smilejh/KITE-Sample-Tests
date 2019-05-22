package io.cosmosoftware.kite.janus.steps;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.report.Reporter;
import io.cosmosoftware.kite.report.Status;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.WebDriver;

import javax.json.*;
import java.util.ArrayList;
import java.util.List;

import static io.cosmosoftware.kite.util.ReportUtils.getStackTrace;
import static org.webrtc.kite.stats.StatsUtils.extractStats;
import static org.webrtc.kite.stats.StatsUtils.getPCStatOvertime;

public class GetStatsStep extends TestStep {


  private final JsonObject getStatsConfig;

  /**
   * for Janus demo testing, the name of the local peer connection are like "pluginHandle" + webrtcStuff.pc in the website source code (https://janus.conf.meetecho.com)
   * list of pluginHandle (corresponding Plugin Demo name):
   *      - echotest (Echo Test)
   *      - streaming (Streaming)
   *      - sfutest (Video Room)
   *      - videocall (Video Call)
   *      - screentest (Screen Share)
   *
   *      see configs file to set the name of the peer connection for each test (key: 'peerConnection')
   */

  public GetStatsStep(WebDriver webDriver, JsonObject getStatsConfig) {
    super(webDriver);
    this.getStatsConfig = getStatsConfig;
  }
  
  
  @Override
  public String stepDescription() {
    return "GetStats";
  }
  
  @Override
  protected void step() throws KiteTestException {
    logger.info("Getting WebRTC stats via getStats");

    try {
      List<JsonObject> stats = getPCStatOvertime(webDriver, getStatsConfig);
      JsonObject sentStats = stats.get(0);
      JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
      List<JsonObject> receivedStats = new ArrayList<>();
      for (int i = 1; i < stats.size(); i++) {
        JsonObject receivedObject = stats.get(i);
        receivedStats.add(receivedObject);
        arrayBuilder.add(receivedObject);
      }
      JsonObject json = extractStats(sentStats, receivedStats);
      JsonObjectBuilder builder = Json.createObjectBuilder();
      builder.add("local", sentStats);
      builder.add("remote", arrayBuilder);
      Reporter.getInstance().jsonAttachment(report, "getStatsRaw", builder.build());
      Reporter.getInstance().jsonAttachment(report, "getStatsSummary", json);
    } catch (Exception e) {
      logger.error(getStackTrace(e));
      throw new KiteTestException("Failed to getStats", Status.BROKEN, e);
    }
  }
}

package io.cosmosoftware.kite.janus.steps;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.report.Reporter;
import io.cosmosoftware.kite.report.Status;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.WebDriver;

import javax.json.*;
import java.util.ArrayList;
import java.util.List;

import static org.webrtc.kite.stats.StatsUtils.extractStats;
import static org.webrtc.kite.stats.StatsUtils.getPCStatOvertime;

public class GetStatsStep extends TestStep {




  private final int numberOfParticipants;
  private final int statsCollectionTime;
  private final int statsCollectionInterval;
  private final JsonArray selectedStats;

  public GetStatsStep(WebDriver webDriver, int numberOfParticipants, int statsCollectionTime,
                      int statsCollectionInterval, JsonArray selectedStats) {
    super(webDriver);
    this.numberOfParticipants = numberOfParticipants;
    this.statsCollectionTime = statsCollectionTime;
    this.statsCollectionInterval = statsCollectionInterval;
    this.selectedStats = selectedStats;
  }
  
  
  @Override
  public String stepDescription() {
    return "GetStats";
  }
  
  @Override
  protected void step() throws KiteTestException {
    logger.info("Getting WebRTC stats via getStats");
    try {
      JsonObject sentStats =
        getPCStatOvertime(webDriver, "window.pc", statsCollectionTime, statsCollectionInterval,
          selectedStats);
      JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
      List<JsonObject> receivedStats = new ArrayList<>();
      for (int i = 1; i < numberOfParticipants; i++) {
        JsonObject receivedObject = getPCStatOvertime(webDriver,
          "window.remotePc[" + (i-1) + "]",
          statsCollectionTime,
          statsCollectionInterval,
          selectedStats);
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
      e.printStackTrace();
      throw new KiteTestException("Failed to getStats", Status.BROKEN, e);
    }
  }
}

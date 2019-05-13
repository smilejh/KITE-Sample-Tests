package io.cosmosoftware.kite.mediasoup.steps;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.report.Reporter;
import io.cosmosoftware.kite.report.Status;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.WebDriver;
import org.webrtc.kite.stats.StatsUtils;

import javax.json.*;
import java.util.ArrayList;
import java.util.List;

public class GetStatsStep extends TestStep {

  private final int numberOfParticipants;
  private final int statsCollectionTime;
  private final int statsCollectionInterval;
  private final JsonArray selectedStats;

  public GetStatsStep(
      WebDriver webDriver,
      int numberOfParticipants,
      int statsCollectionTime,
      int statsCollectionInterval,
      JsonArray selectedStats) {
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
          StatsUtils.getPCStatOvertime(
              webDriver, "window.PC1", statsCollectionTime, statsCollectionInterval, selectedStats);
      JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
      List<JsonObject> receivedStats = new ArrayList<>();
      JsonObject receivedObject =
          StatsUtils.getPCStatOvertime(
              webDriver, "window.PC2", statsCollectionTime, statsCollectionInterval, selectedStats);
      receivedStats.add(receivedObject);
      arrayBuilder.add(receivedObject);

      JsonObject json = StatsUtils.extractStats(sentStats, receivedStats);
      System.out.println(json);
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

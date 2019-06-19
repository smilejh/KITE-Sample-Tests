package io.cosmosoftware.kite.mediasoup.steps;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.interfaces.Runner;
import io.cosmosoftware.kite.report.Reporter;
import io.cosmosoftware.kite.report.Status;
import io.cosmosoftware.kite.steps.TestStep;
import org.webrtc.kite.stats.StatsUtils;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.ArrayList;
import java.util.List;

import static org.webrtc.kite.stats.StatsUtils.getPCStatOvertime;

public class GetStatsStep extends TestStep {

  private final JsonObject getStatsConfig;

  public GetStatsStep(Runner runner, JsonObject getStatsConfig) {
    super(runner);
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
      JsonObject receivedObject = stats.get(1);
      JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
      List<JsonObject> receivedStats = new ArrayList<>();
      
      receivedStats.add(stats.get(1));
      arrayBuilder.add(receivedObject);

      JsonObject json = StatsUtils.extractStats(sentStats, receivedStats);
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

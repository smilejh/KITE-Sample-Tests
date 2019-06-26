package io.cosmosoftware.kite.janus.steps;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.interfaces.Runner;
import io.cosmosoftware.kite.janus.pages.JanusPage;
import io.cosmosoftware.kite.report.Reporter;
import io.cosmosoftware.kite.report.Status;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.WebDriver;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static io.cosmosoftware.kite.util.ReportUtils.getStackTrace;
import static org.webrtc.kite.stats.StatsUtils.*;

public class GetStatsStep extends TestStep {


  private final JsonObject getStatsConfig;
  private final boolean sfu;
  private final JanusPage janusPage;

  /**
   * for Janus demo testing, the name of the local peer connection are like "pluginHandle" + webrtcStuff.pc in the website source code (https://janus.conf.meetecho.com)
   * list of pluginHandle (corresponding Plugin Demo name):
   * - echotest (Echo Test)
   * - streaming (Streaming)
   * - sfutest (Video Room)
   * - videocall (Video Call)
   * - screentest (Screen Share)
   * <p>
   * see configs file to set the name of the peer connection for each test (key: 'peerConnection')
   */

  public GetStatsStep(Runner runner, JsonObject getStatsConfig, boolean sfu, JanusPage janusPage) {
    super(runner);
    this.janusPage = janusPage;
    this.getStatsConfig = getStatsConfig;
    this.sfu = sfu;
  }


  @Override
  public String stepDescription() {
    return "GetStats";
  }

  @Override
  protected void step() throws KiteTestException {
    logger.info("Getting WebRTC stats via getStats");
    LinkedHashMap<String, String> results = new LinkedHashMap<>();
    try {
      if (sfu) {
        List<String> remotePC = janusPage.getRemotePC();
        JsonObject sentStats = getPCStatOvertime(webDriver,
            getStatsConfig.getJsonArray("peerConnections").getString(0),
            getStatsConfig.getInt("statsCollectionTime"),
            getStatsConfig.getInt("statsCollectionInterval"),
            getStatsConfig.getJsonArray("selectedStats"));
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        List<JsonObject> receivedStats = new ArrayList<>();
        for (int i = 1; i < remotePC.size() + 1; i++) {
          JsonObject receivedObject = getPCStatOvertime(webDriver,
              remotePC.get(i - 1),
              getStatsConfig.getInt("statsCollectionTime"),
              getStatsConfig.getInt("statsCollectionInterval"),
              getStatsConfig.getJsonArray("selectedStats"));
          receivedStats.add(receivedObject);
          arrayBuilder.add(receivedObject);
        }
        JsonObject json = extractStats(sentStats, receivedStats);
        results = statsHashMap(json, remotePC.size());
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("local", sentStats);
        builder.add("remote", arrayBuilder);
        reporter.jsonAttachment(report, "getStatsRaw", builder.build());
        reporter.jsonAttachment(report, "getStatsSummary", json);
      } else {
        JsonObject stats = getPCStatOvertime(webDriver, getStatsConfig).get(0);
        JsonObject statsSummary = buildstatSummary(stats, getStatsConfig.getJsonArray("selectedStats"));
        results = statsHashMap(statsSummary);
        reporter.jsonAttachment(report, "getStatsRaw", stats);
        reporter.jsonAttachment(this.report, "Stats Summary", statsSummary);
      }

    } catch (Exception e) {
      logger.error(getStackTrace(e));
      throw new KiteTestException("Failed to getStats", Status.BROKEN, e);
    } finally {
      this.setCsvResult(results);
    }

  }

}


package io.cosmosoftware.kite.janus.steps;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.janus.pages.JanusPage;
import io.cosmosoftware.kite.report.Reporter;
import io.cosmosoftware.kite.report.Status;
import io.cosmosoftware.kite.steps.TestStep;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.webrtc.kite.Utils;
import org.webrtc.kite.stats.StatsUtils;

import javax.json.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import static io.cosmosoftware.kite.util.ReportUtils.getStackTrace;
import static javax.json.stream.JsonCollectors.toJsonObject;
import static org.glassfish.json.JsonUtil.toJson;
import static org.webrtc.kite.stats.StatsUtils.*;
import static org.webrtc.kite.stats.StatsUtils.getPCStatOvertime;

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

  public GetStatsStep(WebDriver webDriver, JsonObject getStatsConfig, boolean sfu, JanusPage janusPage) {
    super(webDriver);
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
        Reporter.getInstance().jsonAttachment(report, "getStatsRaw", builder.build());
        Reporter.getInstance().jsonAttachment(report, "getStatsSummary", json);
      } else {
        JsonObject stats = getPCStatOvertime(webDriver, getStatsConfig).get(0);
        JsonObject statsSummary = buildstatSummary(stats, getStatsConfig.getJsonArray("selectedStats"));
        results = statsHashMap(statsSummary);
        Reporter.getInstance().jsonAttachment(report, "getStatsRaw", stats);
        Reporter.getInstance().jsonAttachment(this.report, "Stats Summary", statsSummary);
      }

    } catch (Exception e) {
      logger.error(getStackTrace(e));
      throw new KiteTestException("Failed to getStats", Status.BROKEN, e);
    } finally {
      this.setCsvResult(results);
    }

  }

}


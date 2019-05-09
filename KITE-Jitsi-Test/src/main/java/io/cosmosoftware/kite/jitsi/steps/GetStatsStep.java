package io.cosmosoftware.kite.jitsi.steps;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.jitsi.pages.MeetingPage;
import io.cosmosoftware.kite.report.Reporter;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import javax.json.JsonArray;
import javax.json.JsonObject;

import static org.webrtc.kite.stats.StatsUtils.extractStats;
import static org.webrtc.kite.stats.StatsUtils.getPCStatOvertime;

public class GetStatsStep extends TestStep {
  private int statsCollectionTime;
  private int statsCollectionInterval;
  private JsonArray selectedStats;

  public GetStatsStep(
      WebDriver webDriver, int durationInSeconds, int intervalInSeconds, JsonArray selectedStats) {
    super(webDriver);
    this.statsCollectionTime = durationInSeconds;
    this.statsCollectionInterval = intervalInSeconds;
    this.selectedStats = selectedStats;
  }

  @Override
  public String stepDescription() {
    return "Getting Jitsi conference statistics";
  }

  @Override
  protected void step() throws KiteTestException {
    MeetingPage meetingPage = new MeetingPage(webDriver, logger);
    ((JavascriptExecutor) webDriver).executeScript(meetingPage.getPeerConnectionScript());
    JsonObject stats =
        getPCStatOvertime(
            webDriver,
            "window.peerConnections[0]",
            statsCollectionTime,
            statsCollectionInterval,
            selectedStats);
    JsonObject statsSummary = extractStats(stats, "both").build();
    Reporter.getInstance().jsonAttachment(report, "getStatsRaw", stats);
    Reporter.getInstance().jsonAttachment(report, "getStatsSummary", statsSummary);
  }
}

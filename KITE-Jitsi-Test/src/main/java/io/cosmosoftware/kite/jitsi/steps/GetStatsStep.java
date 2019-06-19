package io.cosmosoftware.kite.jitsi.steps;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.interfaces.Runner;
import io.cosmosoftware.kite.jitsi.pages.MeetingPage;
import io.cosmosoftware.kite.report.Reporter;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.JavascriptExecutor;

import javax.json.JsonObject;

import static org.webrtc.kite.stats.StatsUtils.extractStats;
import static org.webrtc.kite.stats.StatsUtils.getPCStatOvertime;

public class GetStatsStep extends TestStep {
  
  private final JsonObject getStatsConfig;
  private final MeetingPage meetingPage;

  public GetStatsStep(Runner runner, JsonObject getStatsConfig) {
    super(runner);
    this.getStatsConfig = getStatsConfig;
    this.meetingPage = new MeetingPage(runner);
  }

  @Override
  public String stepDescription() {
    return "Getting Jitsi conference statistics";
  }

  @Override
  protected void step() throws KiteTestException {
    ((JavascriptExecutor) webDriver).executeScript(meetingPage.getPeerConnectionScript());
    JsonObject rawStats = getPCStatOvertime(webDriver, getStatsConfig).get(0);
    JsonObject statsSummary = extractStats(rawStats, "both").build();
    Reporter.getInstance().jsonAttachment(report, "getStatsRaw", rawStats);
    Reporter.getInstance().jsonAttachment(report, "getStatsSummary", statsSummary);
  }
}

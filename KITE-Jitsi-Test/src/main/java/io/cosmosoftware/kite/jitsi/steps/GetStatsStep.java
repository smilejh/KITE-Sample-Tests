package io.cosmosoftware.kite.jitsi.steps;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.jitsi.pages.MeetingPage;
import io.cosmosoftware.kite.report.Reporter;
import io.cosmosoftware.kite.steps.TestStep;
import org.webrtc.kite.stats.StatsUtils;
import org.openqa.selenium.WebDriver;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

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
    try {

      JsonObject rawStats = meetingPage.getPCStatOvertime(webDriver,statsCollectionTime,statsCollectionInterval,selectedStats);
      System.out.println(rawStats);
      JsonObject formattedStats = meetingPage.buildStatSummary(rawStats);
      Reporter.getInstance().jsonAttachment(this.report, "Peer connection's stats", rawStats);
      Reporter.getInstance().jsonAttachment(this.report, "Stats summary", formattedStats);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

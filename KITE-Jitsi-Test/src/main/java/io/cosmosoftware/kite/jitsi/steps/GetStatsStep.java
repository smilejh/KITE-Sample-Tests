package io.cosmosoftware.kite.jitsi.steps;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.jitsi.pages.MeetingPage;
import io.cosmosoftware.kite.report.Reporter;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.WebDriver;

import javax.json.JsonArray;
import javax.json.JsonObject;

public class GetStatsStep extends TestStep {
  private int durationInSeconds;
  private int intervalInSeconds;
  private JsonArray selectedStats;

  public GetStatsStep(WebDriver webDriver, int durationInSeconds, int intervalInSeconds, JsonArray selectedStats) {
    super(webDriver);
    this.durationInSeconds = durationInSeconds;
    this.intervalInSeconds = intervalInSeconds;
    this.selectedStats = selectedStats;
  }

  @Override
  public String stepDescription() {
    return "Getting Jitsi conference statistics";
  }

  @Override
  protected void step() throws KiteTestException {
    MeetingPage meetingPage = new MeetingPage(webDriver, logger);
    try{
    String stats =
        meetingPage.getPCStatOverTime(webDriver, durationInSeconds, intervalInSeconds, selectedStats);
        Reporter.getInstance().textAttachment(this.report, "Peer connection's stats", stats,"false");
    }catch(Exception e){
      e.printStackTrace();
    }
  }
}

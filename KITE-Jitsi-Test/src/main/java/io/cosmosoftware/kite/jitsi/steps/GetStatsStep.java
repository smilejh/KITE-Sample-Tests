package io.cosmosoftware.kite.jitsi.steps;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.jitsi.KiteJitsiTest;
import io.cosmosoftware.kite.jitsi.pages.MeetingPage;
import io.cosmosoftware.kite.report.Reporter;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.WebDriver;

import javax.json.JsonObject;

public class GetStatsStep extends TestStep {
  private int durationInSeconds;
  private int intervalInSeconds;
  public GetStatsStep(WebDriver webDriver, int durationInSeconds, int intervalInSeconds) {
    super(webDriver);
    this.durationInSeconds = durationInSeconds;
    this.intervalInSeconds = intervalInSeconds;
  }
  @Override
  public String stepDescription() {
    return "Getting Jitsi conference statistics";
  }

  @Override
  protected void step() throws KiteTestException {
    MeetingPage meetingPage = new MeetingPage(webDriver, logger);
    JsonObject stats = meetingPage.getPCStatOverTime(webDriver, durationInSeconds, intervalInSeconds);
    System.out.println(stats);
//    JsonObject stats = meetingPage.getPCStatOverTime(webDriver, durationInSeconds, intervalInSeconds);
    Reporter.getInstance().jsonAttachment(this.report, "Peer connection's stats", stats);
  }
}

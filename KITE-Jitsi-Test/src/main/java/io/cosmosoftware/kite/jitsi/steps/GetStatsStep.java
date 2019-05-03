package io.cosmosoftware.kite.jitsi.steps;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.jitsi.pages.MeetingPage;
import io.cosmosoftware.kite.report.Reporter;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.WebDriver;

import static io.cosmosoftware.kite.stats.GetStatsUtils.getStatsOnce;

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
    try {
      String rawStats = getStatsOnce("jitsi", webDriver).toString().replaceAll("=", ":");
      System.out.println(rawStats);
      Object formattedStats = meetingPage.formatToJsonString(rawStats);
      System.out.println(formattedStats);
    } catch (Exception e) {
      e.printStackTrace();
    }

    //    JsonObject stats = meetingPage.getPCStatOverTime(webDriver, durationInSeconds,
    // intervalInSeconds);
//    Reporter.getInstance().jsonAttachment(this.report, "Peer connection's stats", stats);
  }
}

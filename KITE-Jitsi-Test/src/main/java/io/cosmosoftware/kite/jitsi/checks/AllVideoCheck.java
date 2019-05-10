package io.cosmosoftware.kite.jitsi.checks;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.jitsi.pages.MeetingPage;
import io.cosmosoftware.kite.report.Reporter;
import io.cosmosoftware.kite.report.Status;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.WebDriver;

import static io.cosmosoftware.kite.entities.Timeouts.ONE_SECOND_INTERVAL;
import static io.cosmosoftware.kite.util.TestUtils.videoCheck;
import static io.cosmosoftware.kite.util.TestUtils.waitAround;

public class AllVideoCheck extends TestStep {

  public AllVideoCheck(WebDriver webDriver) {
    super(webDriver);
  }

  @Override
  public String stepDescription() {
    return "Check the other videos are being received OK";
  }

  @Override
  protected void step() throws KiteTestException {
    try {
      final MeetingPage meetingPage = new MeetingPage(this.webDriver, logger);
      // wait a while to allow all videos to load.
      waitAround(meetingPage.getNumberOfParticipants() * 3 * ONE_SECOND_INTERVAL);
      meetingPage.clickVideoToggle();
      logger.info("Looking for video elements");
      if (meetingPage.numberOfVideos() < meetingPage.getNumberOfParticipants()) {
        throw new KiteTestException(
            "Unable to find "
                + meetingPage.getNumberOfParticipants()
                + " <video> element on the page. No video found = "
                + meetingPage.numberOfVideos(),
            Status.FAILED);
      }
      String videoCheck = "";
      boolean error = false;
      for (int i = 1; i < meetingPage.getNumberOfParticipants(); i++) {
        String v = videoCheck(webDriver, i);
        videoCheck += v;
        if (i < meetingPage.getNumberOfParticipants() - 1) {
          videoCheck += "|";
        }
        if (!"video".equalsIgnoreCase(v)) {
          error = true;
        }
      }
      if (error) {
        Reporter.getInstance().textAttachment(report, "Reveived Videos", videoCheck, "plain");
        throw new KiteTestException("Some videos are still or blank: " + videoCheck, Status.FAILED);
      }
    } catch (KiteTestException e) {
      throw e;
    } catch (Exception e) {
      throw new KiteTestException("Error looking for the video", Status.BROKEN, e);
    }
  }
}

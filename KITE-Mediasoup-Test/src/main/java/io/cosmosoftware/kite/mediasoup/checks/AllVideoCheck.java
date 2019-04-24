package io.cosmosoftware.kite.mediasoup.checks;

import io.cosmosoftware.kite.entities.Timeouts;
import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.mediasoup.pages.MediasoupPage;
import io.cosmosoftware.kite.report.Reporter;
import io.cosmosoftware.kite.report.Status;
import io.cosmosoftware.kite.steps.TestStep;
import io.cosmosoftware.kite.util.TestUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class AllVideoCheck extends TestStep {

  
  private final int numberOfParticipants;

  public AllVideoCheck(WebDriver webDriver, int numberOfParticipants) {
    super(webDriver);
    this.numberOfParticipants = numberOfParticipants;
  }

  @Override
  public String stepDescription() {
    return "Check the other videos are being received OK";
  }

  @Override
  protected void step() throws KiteTestException {
    final MediasoupPage mediasoupPage = new MediasoupPage(this.webDriver, logger);
    try {
      //wait a while to allow all videos to load.
      TestUtils.waitAround(numberOfParticipants * 3 * Timeouts.ONE_SECOND_INTERVAL);
      logger.info("Looking for video elements");
      List<WebElement> videos = mediasoupPage.getVideoElements();
      if (videos.size() < numberOfParticipants) {
        throw new KiteTestException(
            "Unable to find " + numberOfParticipants + " <video> element on the page. No video found = "
              + videos.size(), Status.FAILED);
      }
      String videoCheck = "";
      boolean error = false;
      for (int i = 1; i < numberOfParticipants; i++) {
        String v = TestUtils.videoCheck(webDriver, i);
        videoCheck += v;
        if (i < numberOfParticipants - 1) {
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

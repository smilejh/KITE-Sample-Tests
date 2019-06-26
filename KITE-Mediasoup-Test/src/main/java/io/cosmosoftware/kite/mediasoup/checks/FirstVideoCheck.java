package io.cosmosoftware.kite.mediasoup.checks;

import io.cosmosoftware.kite.entities.Timeouts;
import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.interfaces.Runner;
import io.cosmosoftware.kite.mediasoup.pages.MediasoupPage;
import io.cosmosoftware.kite.report.Reporter;
import io.cosmosoftware.kite.report.Status;
import io.cosmosoftware.kite.steps.TestStep;
import io.cosmosoftware.kite.util.TestUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static io.cosmosoftware.kite.util.TestUtils.waitAround;

public class FirstVideoCheck extends TestStep {


  private final MediasoupPage mediasoupPage;

  public FirstVideoCheck(Runner runner) {
    super(runner);
    this.mediasoupPage = new MediasoupPage(runner);
  }

  @Override
  public String stepDescription() {
    return "Check the first video is being sent OK";
  }

  @Override
  protected void step() throws KiteTestException {
    try {
      waitAround(3* Timeouts.ONE_SECOND_INTERVAL);
      logger.info("Looking for video object");
      List<WebElement> videos = mediasoupPage.getVideoElements();
      if (videos.isEmpty()) {
        throw new KiteTestException(
            "Unable to find any <video> element on the page", Status.FAILED);
      }

      String videoCheck = TestUtils.videoCheck(webDriver, 0);
      if (!"video".equalsIgnoreCase(videoCheck)) {
        reporter.textAttachment(report, "Sent Video", videoCheck, "plain");
        throw new KiteTestException("The first video is " + videoCheck, Status.FAILED);
      }
    } catch (KiteTestException e) {
      throw e;
    } catch (Exception e) {
      throw new KiteTestException("Error looking for the video", Status.BROKEN, e);
    }
  }
}

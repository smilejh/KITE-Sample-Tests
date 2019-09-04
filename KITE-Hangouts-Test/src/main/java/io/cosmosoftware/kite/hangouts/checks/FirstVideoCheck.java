package io.cosmosoftware.kite.hangouts.checks;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.hangouts.pages.MainPage;
import io.cosmosoftware.kite.interfaces.Runner;
import io.cosmosoftware.kite.report.Status;
import io.cosmosoftware.kite.steps.StepPhase;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.WebElement;

import java.util.List;

import static io.cosmosoftware.kite.entities.Timeouts.SHORT_TIMEOUT_IN_SECONDS;
import static io.cosmosoftware.kite.util.ReportUtils.saveScreenshotPNG;
import static io.cosmosoftware.kite.util.ReportUtils.timestamp;
import static io.cosmosoftware.kite.util.TestUtils.videoCheck;

public class FirstVideoCheck extends TestStep {

  private final MainPage mainPage;

  public FirstVideoCheck(Runner runner, MainPage mainPage) {
    super(runner);
    setStepPhase(StepPhase.ALL);
    this.mainPage = mainPage;
  }

  @Override
  public String stepDescription() {
    return "Check the first video is being sent OK";
  }

  @Override
  protected void step() throws KiteTestException {
    try {
      logger.info("Looking for video object");
      mainPage.videoIsPublishing(SHORT_TIMEOUT_IN_SECONDS);
      List<WebElement> videos = mainPage.getVideoElements();
      if (videos.isEmpty()) {
        throw new KiteTestException(
            "Unable to find any <video> element on the page", Status.FAILED);
      }
      //first video is the fullscreen
      //second video is "display": "none"
      //third video is "You" (the publisher)
      final int FIRST_VIDEO_INDEX = videos.size() > 1 ? 2 : 0;
      String videoCheck = videoCheck(webDriver, FIRST_VIDEO_INDEX);
      if (!"video".equalsIgnoreCase(videoCheck)) {
        reporter.screenshotAttachment(report,
          "FirstVideoCheck_" + timestamp(), saveScreenshotPNG(webDriver));
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

package io.cosmosoftware.kite.simulcast.checks;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.interfaces.Runner;
import io.cosmosoftware.kite.report.Reporter;
import io.cosmosoftware.kite.report.Status;
import io.cosmosoftware.kite.simulcast.pages.SimulcastPageBase;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static io.cosmosoftware.kite.entities.Timeouts.ONE_SECOND_INTERVAL;
import static io.cosmosoftware.kite.util.ReportUtils.saveScreenshotPNG;
import static io.cosmosoftware.kite.util.ReportUtils.timestamp;
import static io.cosmosoftware.kite.util.TestUtils.videoCheck;
import static io.cosmosoftware.kite.util.TestUtils.waitAround;

public abstract class VideoCheckBase extends TestStep {

  protected final SimulcastPageBase page;

  public VideoCheckBase(Runner runner, SimulcastPageBase page) {
    super(runner);
    this.page = page;
  }

  protected void step(String direction) throws KiteTestException {
    try {
      List<WebElement> videos = page.getVideoElements();
      if (videos.isEmpty()) {
        throw new KiteTestException(
            "Unable to find any <video> element on the page", Status.FAILED);
      }
      String videoCheck = videoCheck(webDriver, "sent".equalsIgnoreCase(direction) ? 0 : 1);
      int ct = 0;
      while(!"video".equalsIgnoreCase(videoCheck) && ct < 3) {
        videoCheck = videoCheck(webDriver, 1);
        ct++;
        waitAround(3 * ONE_SECOND_INTERVAL);
      }
      if (!"video".equalsIgnoreCase(videoCheck)) {
        reporter.textAttachment(report, direction +" video", videoCheck, "plain");
        reporter.screenshotAttachment(report,
          direction + "_video_" + timestamp(), saveScreenshotPNG(webDriver));
        throw new KiteTestException("The " + direction + " video is " + videoCheck, Status.FAILED, null, true);
      }
      logger.info(direction + " video is OK");
    } catch (KiteTestException e) {
      throw e;
    } catch (Exception e) {
      throw new KiteTestException("Error looking for the video", Status.BROKEN, e);
    }
  }
}

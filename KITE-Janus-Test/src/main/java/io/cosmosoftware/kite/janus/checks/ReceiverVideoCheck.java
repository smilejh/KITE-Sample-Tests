package io.cosmosoftware.kite.simulcast.checks;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.janus.pages.JanusPage;
import io.cosmosoftware.kite.report.Reporter;
import io.cosmosoftware.kite.report.Status;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static io.cosmosoftware.kite.entities.Timeouts.ONE_SECOND_INTERVAL;
import static io.cosmosoftware.kite.util.ReportUtils.saveScreenshotPNG;
import static io.cosmosoftware.kite.util.ReportUtils.timestamp;
import static io.cosmosoftware.kite.util.TestUtils.videoCheck;
import static io.cosmosoftware.kite.util.TestUtils.waitAround;

public class ReceiverVideoCheck extends TestStep {


  public ReceiverVideoCheck(WebDriver webDriver) {
    super(webDriver);
  }

  
  @Override
  protected void step() throws KiteTestException {
    try {
      final JanusPage janusPage = new JanusPage(this.webDriver, logger);
      List<WebElement> videos = janusPage.getVideoElements();
      if (videos.isEmpty()) {
        throw new KiteTestException(
            "Unable to find any <video> element on the page", Status.FAILED);
      }
      String videoCheck = videoCheck(webDriver,1);
      int ct = 0;
      while(!"video".equalsIgnoreCase(videoCheck) && ct < 3) {
        videoCheck = videoCheck(webDriver, 1);
        ct++;
        waitAround(3 * ONE_SECOND_INTERVAL);
      }
      if (!"video".equalsIgnoreCase(videoCheck)) {
        Reporter.getInstance().textAttachment(report, "received" +" video", videoCheck, "plain");
        Reporter.getInstance().screenshotAttachment(report,
            "received" + "_video_" + timestamp(), saveScreenshotPNG(webDriver));
        throw new KiteTestException("The received video is " + videoCheck, Status.FAILED, null, true);
      }
      logger.info("received video is OK");
    } catch (KiteTestException e) {
      throw e;
    } catch (Exception e) {
      throw new KiteTestException("Error looking for the video", Status.BROKEN, e);
    }
  }

  @Override
  public String stepDescription() {
    return "Check the first video is being received OK";
  }
}

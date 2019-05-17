package io.cosmosoftware.kite.janus.checks;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.janus.pages.JanusPage;
import io.cosmosoftware.kite.report.Reporter;
import io.cosmosoftware.kite.report.Status;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static io.cosmosoftware.kite.entities.Timeouts.ONE_SECOND_INTERVAL;
import static io.cosmosoftware.kite.util.TestUtils.videoCheck;
import static io.cosmosoftware.kite.util.TestUtils.waitAround;

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
    try {
      final JanusPage janusPage = new JanusPage(this.webDriver, logger);
      //wait a while to allow all videos to load.;
      List<WebElement> videos = janusPage.getVideoElements();
      boolean flag = true;
      for (int i = 1; i < numberOfParticipants; i++) {
        String v2 = videoCheck(webDriver, i);
        flag = flag && "video".equalsIgnoreCase(v2);
      }
      int waitingTime = 0;
      while((videos.size() < numberOfParticipants || !flag) && waitingTime < 10*numberOfParticipants) {
        flag = true;
        waitAround(ONE_SECOND_INTERVAL);
        videos = janusPage.getVideoElements();
        for (int i = 1; i < numberOfParticipants; i++) {
          String v2 = videoCheck(webDriver, i);
          flag = flag && "video".equalsIgnoreCase(v2);
        }
        waitingTime += 1;
      }
      if (videos.size() < numberOfParticipants) {
        throw new KiteTestException(
            "Unable to find " + numberOfParticipants + " <video> element on the page. No video found = "
              + videos.size(), Status.FAILED);
      }
      String videoCheck = "";
      boolean error = false;
      for (int i = 1; i < numberOfParticipants; i++) {
        String v = videoCheck(webDriver, i);
        videoCheck += v;
        if (i < numberOfParticipants - 1) {
          videoCheck += "|";
        }
        if (!"video".equalsIgnoreCase(v)) {
          error = true;
        }
      }
      if (error) {
        Reporter.getInstance().textAttachment(report, "Received Videos", videoCheck, "plain");
        throw new KiteTestException("Some videos are still or blank: " + videoCheck, Status.FAILED);
      }
    } catch (KiteTestException e) {
      throw e;
    } catch (Exception e) {
      throw new KiteTestException("Error looking for the video", Status.BROKEN, e);
    }
  }
}

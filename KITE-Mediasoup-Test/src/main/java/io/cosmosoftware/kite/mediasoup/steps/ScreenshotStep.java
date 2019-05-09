package io.cosmosoftware.kite.mediasoup.steps;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.report.Reporter;
import io.cosmosoftware.kite.steps.TestStep;
import io.cosmosoftware.kite.util.ReportUtils;
import org.openqa.selenium.WebDriver;

public class ScreenshotStep extends TestStep {

  public ScreenshotStep(WebDriver webDriver) {
    super(webDriver);
  }
  
  
  @Override
  public String stepDescription() {
    return "Get a screenshot";
  }
  
  @Override
  protected void step() throws KiteTestException {
    Reporter.getInstance().screenshotAttachment(report,
      "ScreenshotStep_" + ReportUtils.timestamp(), ReportUtils.saveScreenshotPNG(webDriver));
  }
}

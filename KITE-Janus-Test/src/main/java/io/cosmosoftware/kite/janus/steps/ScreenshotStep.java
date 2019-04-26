package io.cosmosoftware.kite.janus.steps;

import io.cosmosoftware.kite.janus.Scenario;
import io.cosmosoftware.kite.report.Reporter;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.WebDriver;

import static io.cosmosoftware.kite.util.ReportUtils.saveScreenshotPNG;
import static io.cosmosoftware.kite.util.ReportUtils.timestamp;

public class ScreenshotStep extends TestStep {

  public ScreenshotStep(WebDriver webDriver, Scenario scenario) {
    super(webDriver);
    this.setName(getName() + "_" + scenario.getName());
  }

  public ScreenshotStep(WebDriver webDriver) {
    super(webDriver);
  }
  
  
  @Override
  public String stepDescription() {
    return "Get a screenshot";
  }
  
  @Override
  protected void step() {
    Reporter.getInstance().screenshotAttachment(report,
      "ScreenshotStep_" + timestamp(), saveScreenshotPNG(webDriver));
  }
}

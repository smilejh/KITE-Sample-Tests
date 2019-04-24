package io.cosmosoftware.kite.simulcast.checks;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.report.Reporter;
import io.cosmosoftware.kite.simulcast.LoopbackStats;
import io.cosmosoftware.kite.simulcast.pages.SimulcastPageBase;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.WebDriver;

import static io.cosmosoftware.kite.util.ReportUtils.saveScreenshotPNG;
import static io.cosmosoftware.kite.util.ReportUtils.timestamp;

public class GaugesCheck extends TestStep {

  private final SimulcastPageBase loopbackPage;


  private final String rid;
  private final int tid;

  public GaugesCheck(WebDriver webDriver, SimulcastPageBase page, String rid, int tid) {
    super(webDriver);
    this.loopbackPage = page;
    this.rid = rid;
    this.tid = tid;
  }
  
  @Override
  public String stepDescription() {
    return "Gauges values for profile " + rid + tid;
  }
  
  @Override
  protected void step() throws KiteTestException {
    LoopbackStats loopbackStats = loopbackPage.getLoopbackStats();
    Reporter.getInstance().jsonAttachment(report, "stats", loopbackStats.getJson());
    Reporter.getInstance().screenshotAttachment(report,
      "Gauges_" + rid + tid + "_" + timestamp(), saveScreenshotPNG(webDriver));
    loopbackStats.validate(rid, logger);
  }
}

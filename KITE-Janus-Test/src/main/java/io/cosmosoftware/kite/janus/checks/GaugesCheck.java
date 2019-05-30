package io.cosmosoftware.kite.janus.checks;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.janus.*;
import io.cosmosoftware.kite.janus.pages.*;
import io.cosmosoftware.kite.report.Reporter;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.WebDriver;

import static io.cosmosoftware.kite.util.ReportUtils.saveScreenshotPNG;
import static io.cosmosoftware.kite.util.ReportUtils.timestamp;

public class GaugesCheck extends TestStep {

  private final JanusPage janusPage;


  private final String rid;
  private final int tid;

  public GaugesCheck(WebDriver webDriver, JanusPage page, String rid, int tid) {
    super(webDriver);
    this.janusPage = page;
    this.rid = rid;
    this.tid = tid;
  }
  
  @Override
  public String stepDescription() {
    return "Gauges values for profile " + rid + tid;
  }
  
  @Override
  protected void step() throws KiteTestException {
    LoopbackStats loopbackStats = janusPage.getLoopbackStats();
    Reporter.getInstance().jsonAttachment(report, "stats", loopbackStats.getJson());
    Reporter.getInstance().screenshotAttachment(report,
      "Gauges_" + rid + tid + "_" + timestamp(), saveScreenshotPNG(webDriver));
    loopbackStats.validate(rid, logger);
  }
}

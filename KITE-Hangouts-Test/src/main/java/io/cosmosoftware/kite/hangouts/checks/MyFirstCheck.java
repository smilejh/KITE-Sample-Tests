package io.cosmosoftware.kite.hangouts.checks;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.interfaces.Runner;
import io.cosmosoftware.kite.report.Status;
import io.cosmosoftware.kite.steps.TestCheck;

import static io.cosmosoftware.kite.util.ReportUtils.saveScreenshotPNG;

public class MyFirstCheck extends TestCheck {


  private final long randomValue1 = Math.round(Math.random() * 100);
  private final long randomValue2 = Math.round(Math.random() * 100);

  public MyFirstCheck(Runner runner) {
    super(runner);
  }

  @Override
  public String stepDescription() {
    return "MyFirstCheck compares two random values and pass if " + randomValue1 + " < " + randomValue2;
  }

  @Override
  protected void step() throws KiteTestException {
    if (randomValue1 >= randomValue2) {
      throw new KiteTestException("MyFirstCheck failed, " + randomValue1 + " >= " + randomValue2, Status.FAILED);
    }
    reporter.screenshotAttachment(report, saveScreenshotPNG(webDriver));
    logger.info("MyFirstCheck pass, " + randomValue1 + " < " + randomValue2);
  }
}

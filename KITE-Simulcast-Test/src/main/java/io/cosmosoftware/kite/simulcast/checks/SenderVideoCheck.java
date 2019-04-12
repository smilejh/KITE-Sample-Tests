package io.cosmosoftware.kite.simulcast.checks;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.simulcast.pages.SimulcastPageBase;
import org.openqa.selenium.WebDriver;

import static io.cosmosoftware.kite.entities.Timeouts.ONE_SECOND_INTERVAL;
import static io.cosmosoftware.kite.util.TestUtils.waitAround;

public class SenderVideoCheck extends VideoCheckBase {

  public SenderVideoCheck(WebDriver webDriver, SimulcastPageBase page) {
    super(webDriver, page);
  }

  @Override
  public String stepDescription() {
    return "Check the first video is being sent OK";
  }

  @Override
  protected void step() throws KiteTestException {
    waitAround(ONE_SECOND_INTERVAL);
    step("sent");
  }
}

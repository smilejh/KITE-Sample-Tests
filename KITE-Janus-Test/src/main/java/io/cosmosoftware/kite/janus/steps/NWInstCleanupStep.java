package io.cosmosoftware.kite.janus.steps;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.instrumentation.NWInstConfig;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.WebDriver;

import static io.cosmosoftware.kite.util.TestUtils.waitAround;

public class NWInstCleanupStep extends TestStep {

  private final NWInstConfig nwInstConfig;

  public NWInstCleanupStep(WebDriver webDriver, NWInstConfig nwInstConfig) {
    super(webDriver);
    this.nwInstConfig = nwInstConfig;
  }


  @Override
  public String stepDescription() {
    return "Network Instrumentation Clean Up";
  }

  @Override
  protected void step() throws KiteTestException {
    nwInstConfig.cleanUp();
    waitAround(1000);
    logger.info("cleanUp() done.");

  }
}

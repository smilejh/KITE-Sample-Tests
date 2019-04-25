package io.cosmosoftware.kite.janus.steps;

import io.cosmosoftware.kite.axel.Instrumentation;
import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.janus.Scenario;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.WebDriver;

import static io.cosmosoftware.kite.util.TestUtils.waitAround;

public class NWInstCleanupStep extends TestStep {

  private final Scenario scenario;
  private final Instrumentation instrumentation;
  private final int clientId;

  public NWInstCleanupStep(WebDriver webDriver, Scenario scenario, Instrumentation instrumentation, int clientId) {
    super(webDriver);
    this.scenario = scenario;
    this.instrumentation = instrumentation;
    this.clientId= clientId;
  }


  @Override
  public String stepDescription() {
    return "Network Instrumentation Clean Up after scenario " + scenario.getName();
  }

  @Override
  protected void step() throws KiteTestException {
    if (this.clientId == scenario.getClientId()) {
      this.scenario.cleanUp(instrumentation);
      logger.info("Cleaning up scenario for " + this.scenario.getName());
    }
    waitAround(1000);
    logger.info("cleanUp() done.");

  }
}

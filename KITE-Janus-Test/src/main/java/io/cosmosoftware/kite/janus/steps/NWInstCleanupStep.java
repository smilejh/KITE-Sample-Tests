package io.cosmosoftware.kite.janus.steps;

import io.cosmosoftware.kite.instrumentation.Instrumentation;
import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.janus.Scenario;
import io.cosmosoftware.kite.report.Reporter;
import io.cosmosoftware.kite.report.Status;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.WebDriver;

import static io.cosmosoftware.kite.util.ReportUtils.getStackTrace;
import static io.cosmosoftware.kite.util.TestUtils.waitAround;

public class NWInstCleanupStep extends TestStep {

  private final Scenario scenario;
  private final Instrumentation instrumentation;
  private final int clientId;
  private String cleanUpCommand;

  public NWInstCleanupStep(WebDriver webDriver, Scenario scenario, Instrumentation instrumentation, int clientId) {
    super(webDriver);
    this.scenario = scenario;
    this.instrumentation = instrumentation;
    this.clientId= clientId;
    this.setName(getName() + "_" + scenario.getName());
  }


  @Override
  public String stepDescription() {
    return "Network Instrumentation Clean Up after scenario " + scenario.getName();
  }

  @Override
  protected void step() throws KiteTestException {
    try {
      if (this.clientId == scenario.getClientId()) {
        cleanUpCommand = this.scenario.cleanUp(instrumentation);
        logger.info("Cleaning up scenario for " + this.scenario.getName());
        Reporter.getInstance().textAttachment(report, "NW Instrumentation CleanUp for " + scenario.getName() + " on gateway " + scenario.getGateway(), "Command executed : " + cleanUpCommand, "plain");
        if (cleanUpCommand.contains("FAILURE")) {
          throw new KiteTestException("Failed to clean up.", Status.FAILED);
        }
      }
      waitAround(1000);
      logger.info("cleanUp() done.");
    } catch (Exception e) {
      logger.error(getStackTrace(e));
      throw new KiteTestException("Failed to clean up ", Status.BROKEN, e);
    }
  }
}

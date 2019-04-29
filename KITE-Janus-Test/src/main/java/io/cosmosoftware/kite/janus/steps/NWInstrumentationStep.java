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

public class NWInstrumentationStep extends TestStep {

  private final Scenario scenario;
  private final Instrumentation instrumentation;
  private final int clientId;
  private String result;

  public NWInstrumentationStep(WebDriver webDriver, Scenario scenario, Instrumentation instrumentation, int clientId) {
    super(webDriver);
    this.scenario = scenario;
    this.instrumentation = instrumentation;
    this.clientId = clientId;
    this.setName(getName() + "_" + scenario.getName());
  }

  @Override
  public String stepDescription() {
    return "Network Instrumentation for " + scenario.getName();
  }
  
  @Override
  protected void step() throws KiteTestException {
    try {
      Reporter.getInstance().textAttachment(report, "Command on gw " + scenario.getGateway(),
          scenario.getCommand(), "plain");
      waitAround(1000);
      if (this.clientId == scenario.getClientId()) {
        result = scenario.runCommands(instrumentation);
        Reporter.getInstance().textAttachment(report, "Result", result, "plain");
        if (result.contains("FAILURE")) {
          throw new KiteTestException("Failed to execute command.", Status.FAILED);
        }
      }
      waitAround(scenario.getDuration());
    } catch (Exception e) {
      logger.error(getStackTrace(e));
      throw new KiteTestException("Failed to execute command ", Status.BROKEN, e);
    }
  }

}

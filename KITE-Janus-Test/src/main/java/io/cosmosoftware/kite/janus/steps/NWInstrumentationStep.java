package io.cosmosoftware.kite.janus.steps;

import io.cosmosoftware.kite.axel.Instrumentation;
import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.janus.Scenario;
import io.cosmosoftware.kite.report.Reporter;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.WebDriver;

import static io.cosmosoftware.kite.util.TestUtils.waitAround;

public class NWInstrumentationStep extends TestStep {

  private final Scenario scenario;
  private final Instrumentation instrumentation;
  private final int clientId;

  public NWInstrumentationStep(WebDriver webDriver, Scenario scenario, Instrumentation instrumentation, int clientId) {
    super(webDriver);
    this.scenario = scenario;
    this.instrumentation = instrumentation;
    this.clientId = clientId;
  }

  @Override
  public String stepDescription() {
    return "Network Instrumentation for " + scenario.getName();
  }
  
  @Override
  protected void step() throws KiteTestException {
    Reporter.getInstance().textAttachment(report, "NW Instrumentation for " + scenario.getName(), "Command executed : " + scenario.getCommand() + " on gateway " + scenario.getGateway(), "plain");
    waitAround( 1000);
    if (this.clientId == scenario.getClientId()) {
      scenario.runCommands(instrumentation);
    }
    waitAround( 10000);
  }
}

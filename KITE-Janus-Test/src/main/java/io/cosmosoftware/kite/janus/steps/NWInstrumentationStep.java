package io.cosmosoftware.kite.janus.steps;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.instrumentation.Scenario;
import io.cosmosoftware.kite.report.Reporter;
import io.cosmosoftware.kite.report.Status;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.WebDriver;

import static io.cosmosoftware.kite.util.ReportUtils.getStackTrace;
import static io.cosmosoftware.kite.util.TestUtils.waitAround;

public class NWInstrumentationStep extends TestStep {

  private final Scenario scenario;
  private final int clientId;

  public NWInstrumentationStep(WebDriver webDriver, Scenario scenario, int clientId) {
    super(webDriver);
    this.scenario = scenario;
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
        StringBuilder text = new StringBuilder();
        String command;
        for (String gw : this.scenario.getCommandList().keySet()) {
          command = this.scenario.getCommandList().get(gw);
          if (!command.equals("")) {
            text.append("Executing command ").append(command).append("on gateway ").append(gw).append("\n\n");
          }
        }
        Reporter.getInstance().textAttachment(report, "Commands for scenario " + scenario.getName(), text.toString(), "plain");
        waitAround(1000);
        if (this.clientId == scenario.getClientId()) {
          String result = scenario.runCommands();
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

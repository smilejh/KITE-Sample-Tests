package io.cosmosoftware.kite.janus.steps;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.instrumentation.NWInstConfig;
import io.cosmosoftware.kite.report.Reporter;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.WebDriver;

import static io.cosmosoftware.kite.util.TestUtils.waitAround;

public class NWInstrumentationStep extends TestStep {

  private final NWInstConfig nwInstConfig;

  public NWInstrumentationStep(WebDriver webDriver, NWInstConfig nwInstConfig) {
    super(webDriver);
    this.nwInstConfig = nwInstConfig;
  }
  
  
  @Override
  public String stepDescription() {
    return "Network Instrumentation";
  }
  
  @Override
  protected void step() throws KiteTestException {
    Reporter.getInstance().textAttachment(report, "NW Instrumentation", nwInstConfig.toString(), "plain");
    waitAround( 1000);
    nwInstConfig.runGatewayCommands(0);
    waitAround( 10000);
  }
}

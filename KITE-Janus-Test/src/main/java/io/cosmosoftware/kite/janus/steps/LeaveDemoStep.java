package io.cosmosoftware.kite.janus.steps;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.interfaces.Runner;
import io.cosmosoftware.kite.janus.pages.JanusPage;
import io.cosmosoftware.kite.steps.TestStep;

import static io.cosmosoftware.kite.entities.Timeouts.ONE_SECOND_INTERVAL;
import static io.cosmosoftware.kite.util.TestUtils.waitAround;

public class LeaveDemoStep extends TestStep {


  private final JanusPage janusPage;
  
  public LeaveDemoStep(Runner runner) {
    super(runner);
    this.janusPage = new JanusPage(runner);
  }

  @Override
  protected void step() throws KiteTestException {

    janusPage.startOrStopDemo();
    waitAround(3*ONE_SECOND_INTERVAL);
  }

  @Override
  public String stepDescription() {
    return "Leave the janus demo test";
  }
}

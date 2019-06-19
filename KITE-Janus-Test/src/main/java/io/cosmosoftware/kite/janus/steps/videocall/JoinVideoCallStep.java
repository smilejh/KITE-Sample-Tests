package io.cosmosoftware.kite.janus.steps.videocall;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.interfaces.Runner;
import io.cosmosoftware.kite.janus.pages.JanusPage;
import io.cosmosoftware.kite.steps.TestStep;



public class JoinVideoCallStep extends TestStep {

  private final int runnerId;
  private final String testCaseName;
  private final JanusPage janusPage;

  public JoinVideoCallStep(Runner runner, int runnerId, String testCaseName) {

    super(runner);
    this.runnerId = runnerId;
    this.testCaseName = testCaseName;
    this.janusPage = new JanusPage(runner);

  }

  @Override
  public String stepDescription() {
    int runnersPeerId = runnerId/2;
    if (this.runnerId%2 == 0){
      return "Wait for the user Bob" + runnersPeerId + testCaseName + " to answer" ;
    } else {
      return "Answer the call from Alice" + runnersPeerId + testCaseName ;
    }

  }

  @Override
  protected void step() throws KiteTestException {
    if (runnerId%2 == 1){
      janusPage.answerCall();
    }

  }
}

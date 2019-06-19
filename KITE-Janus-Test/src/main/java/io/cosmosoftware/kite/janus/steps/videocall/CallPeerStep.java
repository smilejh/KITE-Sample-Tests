package io.cosmosoftware.kite.janus.steps.videocall;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.interfaces.Runner;
import io.cosmosoftware.kite.janus.pages.JanusPage;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.WebDriver;

public class CallPeerStep extends TestStep {


  private final int runnerId;
  private final String testCaseName;
  private final JanusPage janusPage;

  public CallPeerStep(Runner runner, int runnerId, String testCaseName) {
    super(runner);
    this.runnerId = runnerId;
    this.testCaseName = testCaseName;
    this.janusPage = new JanusPage(runner);
  }

  @Override
  protected void step() throws KiteTestException {
    int runnersPeerId = runnerId/2;

    String peerName = (runnerId%2 == 0)? "Bob" + runnersPeerId + testCaseName: "Alice" + runnersPeerId + testCaseName;

    if ((runnerId%2 == 0)){
      janusPage.fillPeerName(peerName);
      janusPage.callPeer();
    }

  }

  @Override
  public String stepDescription() {
    int runnersPeerId = runnerId/2;

    if (this.runnerId%2 == 0){
      return "Call the user Bob" + runnersPeerId + testCaseName ;
    } else {
      return "Wait for the call from Alice" + runnersPeerId + testCaseName ;
    }

  }
}

package io.cosmosoftware.kite.janus.steps.videocall;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.janus.pages.JanusPage;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.WebDriver;

public class CallPeerStep extends TestStep {


  private final int runnerId;
  private final String testCaseName;

  public CallPeerStep(WebDriver webDriver, int runnerId, String testCaseName) {
    super(webDriver);
    this.runnerId = runnerId;
    this.testCaseName = testCaseName;
  }

  @Override
  protected void step() throws KiteTestException {
    final JanusPage janusPage = new JanusPage(this.webDriver, this.logger);
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

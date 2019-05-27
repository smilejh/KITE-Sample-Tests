package io.cosmosoftware.kite.janus.steps;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.janus.pages.JanusPage;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.WebDriver;

public class CallPeerStep extends TestStep {


  private final String runnerId;
  private final String testCaseName;

  public CallPeerStep(WebDriver webDriver, String runnerId, String testCaseName) {
    super(webDriver);
    this.runnerId = runnerId;
    this.testCaseName = testCaseName;
  }

  @Override
  protected void step() throws KiteTestException {
    final JanusPage janusPage = new JanusPage(this.webDriver, this.logger);

    String peerName = (("000").equalsIgnoreCase(runnerId))? "Bob" + testCaseName: "Alice" + testCaseName;

    if (("000").equalsIgnoreCase(runnerId)){
      janusPage.fillPeerName(peerName);
      janusPage.callPeer();
    }

  }

  @Override
  public String stepDescription() {

    if (("000").equalsIgnoreCase(this.runnerId)){
      return "Call the user Bob" + testCaseName ;
    } else {
      return "Wait for the call from Alice" + testCaseName ;
    }

  }
}

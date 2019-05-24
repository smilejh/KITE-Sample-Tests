package io.cosmosoftware.kite.janus.steps;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.janus.pages.JanusPage;
import io.cosmosoftware.kite.steps.TestStep;
import io.cosmosoftware.kite.util.TestUtils;
import org.openqa.selenium.WebDriver;

import static io.cosmosoftware.kite.entities.Timeouts.ONE_SECOND_INTERVAL;
import static io.cosmosoftware.kite.util.TestUtils.waitAround;

public class JoinVideoCallStep extends TestStep {

  private final String runnerId;
  private final String testCaseName;

public JoinVideoCallStep(WebDriver webDriver, String runnerId, String testCaseName) {

    super(webDriver);
    this.runnerId = runnerId;
    this.testCaseName = testCaseName;

  }

  @Override
  public String stepDescription() {
    return "Register the user on the call and call another user" ;
  }

  @Override
  protected void step() throws KiteTestException {
    final JanusPage janusPage = new JanusPage(this.webDriver, this.logger);

    //for now, tupleSize has to be equal to 2
    //but need to adapt the following lines if we want to enable tupleSize>2 (with
    String callerName = (("000").equalsIgnoreCase(runnerId))? "Alice" + testCaseName : "Bob" + testCaseName;
    String peerName = (("000").equalsIgnoreCase(runnerId))? "Bob" + testCaseName: "Alice" + testCaseName;
    janusPage.fillCallerName(callerName);
    janusPage.registerUser();
    waitAround(ONE_SECOND_INTERVAL);

    if (("000").equalsIgnoreCase(runnerId)){
      janusPage.fillPeerName(peerName);
      janusPage.callPeer();
      waitAround(2*ONE_SECOND_INTERVAL);
    }

    else {
      janusPage.answerCall();
    }
  }
}

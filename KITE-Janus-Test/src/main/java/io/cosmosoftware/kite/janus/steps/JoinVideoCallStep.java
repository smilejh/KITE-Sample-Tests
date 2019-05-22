package io.cosmosoftware.kite.janus.steps;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.janus.pages.JanusPage;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.WebDriver;

import static io.cosmosoftware.kite.entities.Timeouts.ONE_SECOND_INTERVAL;
import static io.cosmosoftware.kite.util.TestUtils.waitAround;

public class JoinVideoCallStep extends TestStep {

//  private final String callerName;
//  private final String peerName;
  private final String driverId;

//  public JoinVideoCallStep(WebDriver webDriver, String callerName, String peerName, String driverId) {
public JoinVideoCallStep(WebDriver webDriver, String driverId) {

    super(webDriver);
//    this.callerName = callerName;
//    this.peerName = peerName;
    this.driverId = driverId;

  }

  @Override
  public String stepDescription() {
    return "Register the user on the call and call another user" ;
  }

  @Override
  protected void step() throws KiteTestException {
    final JanusPage janusPage = new JanusPage(this.webDriver, this.logger);
    String callerName = (("000").equalsIgnoreCase(driverId))? "Alice" : "Bob";
    String peerName = (("000").equalsIgnoreCase(driverId))? "Bob" : "Alice";
    janusPage.fillCallerName(callerName);
    janusPage.registerUser();
    waitAround(ONE_SECOND_INTERVAL);

    if (("000").equalsIgnoreCase(driverId)){
      janusPage.fillPeerName(peerName);
      janusPage.callPeer();
    }
    else {
      janusPage.answerCall();
    }
  }
}

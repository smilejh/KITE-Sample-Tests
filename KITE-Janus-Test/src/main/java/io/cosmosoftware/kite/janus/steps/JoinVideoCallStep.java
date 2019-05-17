package io.cosmosoftware.kite.janus.steps;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.janus.pages.JanusPage;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.WebDriver;

public class JoinVideoCallStep extends TestStep {

  private final String callerName;
  private final String peerName;

  public JoinVideoCallStep(WebDriver webDriver, String callerName, String peerName) {

    super(webDriver);
    this.callerName = callerName;
    this.peerName = peerName;

  }

  @Override
  public String stepDescription() {
    return "Register the user on the call and call another user" ;
  }

  @Override
  protected void step() throws KiteTestException {
    final JanusPage janusPage = new JanusPage(this.webDriver, this.logger);
    janusPage.fillCallerName(callerName);
    janusPage.fillPeerName(peerName);
    janusPage.registerUser();


  }
}

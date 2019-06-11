package io.cosmosoftware.kite.janus.steps.videocall;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.janus.pages.JanusPage;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.WebDriver;



public class JoinVideoCallStep extends TestStep {

  private final int runnerId;
  private final String testCaseName;

  public JoinVideoCallStep(WebDriver webDriver, int runnerId, String testCaseName) {

    super(webDriver);
    this.runnerId = runnerId;
    this.testCaseName = testCaseName;

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
    final JanusPage janusPage = new JanusPage(this.webDriver, this.logger);

    if (runnerId%2 == 1){
      janusPage.answerCall();
    }

  }
}

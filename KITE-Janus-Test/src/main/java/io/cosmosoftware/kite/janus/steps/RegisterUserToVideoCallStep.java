package io.cosmosoftware.kite.janus.steps;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.janus.pages.JanusPage;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.WebDriver;

public class RegisterUserToVideoCallStep extends TestStep {


  private final int runnerId;
  private final String testCaseName;

  public RegisterUserToVideoCallStep(WebDriver webDriver, int runnerId, String testCaseName) {

    super(webDriver);

    this.runnerId = runnerId;
    this.testCaseName = testCaseName;

  }

  @Override
  public String stepDescription() {
    int runnersPeerId = runnerId/2;
    if (this.runnerId%2 == 0){
      return "Register the user Alice" + runnersPeerId + testCaseName;
    } else {
      return "Register the user Bob" + runnersPeerId + testCaseName ;
    }
  }

  @Override
  protected void step() throws KiteTestException {
    final JanusPage janusPage = new JanusPage(this.webDriver, this.logger);
    int runnersPeerId = runnerId/2;

    String callerName = (runnerId%2 == 0)? "Alice" + runnersPeerId + testCaseName : "Bob" + runnersPeerId + testCaseName;
    janusPage.fillCallerName(callerName);
    janusPage.registerUser();

  }
}

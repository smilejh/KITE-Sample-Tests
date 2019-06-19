package io.cosmosoftware.kite.openvidu.steps;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.interfaces.Runner;
import io.cosmosoftware.kite.openvidu.pages.JoinPage;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.WebDriver;

public class SetUserIdStep extends TestStep {
  
  private final String userId;
  private final JoinPage joinPage;

  public SetUserIdStep(Runner runner, String userId) {
    super(runner);
    this.userId = userId;
    this.joinPage = new JoinPage(runner);
  }

  @Override
  public String stepDescription() {
    return "Setting Display Name as " + userId;
  }

  @Override
  protected void step() throws KiteTestException {
    joinPage.enterNickName(userId);
  }
}

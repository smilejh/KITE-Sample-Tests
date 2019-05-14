package io.cosmosoftware.kite.openvidu.steps;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.openvidu.pages.JoinPage;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class SetUserIdStep extends TestStep {
  String userId;

  public SetUserIdStep(WebDriver webDriver, String userId){
    super(webDriver);
    this.userId = userId;
  }

  @Override
  public String stepDescription() {
    return "Setting Display Name as " + userId;
  }

  @Override
  protected void step() throws KiteTestException {
    JoinPage joinPage = new JoinPage(webDriver,logger);
    joinPage.enterNickName(userId);
  }
}

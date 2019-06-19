package io.cosmosoftware.kite.openvidu.pages;

import io.cosmosoftware.kite.interfaces.Runner;
import io.cosmosoftware.kite.pages.BasePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class JoinPage extends BasePage {

  public JoinPage(Runner runner) {
    super(runner);
  }

  @FindBy(id = "mat-input-0")
  private WebElement nickNameBox;

  @FindBy(id = "joinButton")
  private WebElement joinButton;

  public void clickJoinButton() {
    joinButton.click();
  }

  public void enterNickName(String nickName) {
    nickNameBox.clear();
    nickNameBox.sendKeys(nickName);
  }
}

package io.cosmosoftware.kite.openvidu.pages;

import io.cosmosoftware.kite.pages.BasePage;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class JoinPage extends BasePage {

  public JoinPage(WebDriver webDriver, Logger logger) {
    super(webDriver, logger);
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

package io.cosmosoftware.kite.jitsi.pages;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.jitsi.KiteJitsiTest;
import io.cosmosoftware.kite.pages.BasePage;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.apache.log4j.Logger;

public class JoinPage extends BasePage {
  public JoinPage(WebDriver webDriver, Logger logger){
    super(webDriver,  logger);
  }


  public void joinRoom(String s){
    String window = webDriver.getWindowHandle();
    ((JavascriptExecutor) webDriver).executeScript("alert('Test')");
    webDriver.switchTo().alert().accept();
    webDriver.switchTo().window(window);
    webDriver.get(s);
    try{
    Thread.sleep(10000);
    }catch (Exception e){
      e.printStackTrace();
    }
  }
}

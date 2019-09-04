package io.cosmosoftware.kite.hangouts.steps;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.hangouts.pages.MainPage;
import io.cosmosoftware.kite.interfaces.Runner;
import io.cosmosoftware.kite.steps.TestStep;

import static io.cosmosoftware.kite.entities.Timeouts.ONE_SECOND_INTERVAL;
import static io.cosmosoftware.kite.util.TestUtils.waitAround;

public class OpenUrlStep extends TestStep {
  
  private final String url;
  private final MainPage mainPage;
  private final String user;
  private final String pass;
  
  
  
  public OpenUrlStep(Runner runner, MainPage mainPage, String url, String user, String pass) {
    super(runner);
    this.url = url;
    this.user = user;
    this.pass = pass;
    this.mainPage = mainPage;
  }
  
  
  @Override
  public String stepDescription() {
    return "Login to " + url + ", user: " + user;
  }
  
  @Override
  protected void step() throws KiteTestException {
    mainPage.open(url);
    mainPage.clickSignIn();
    mainPage.enterEmail(user);
    mainPage.enterPassword(pass);
    waitAround(ONE_SECOND_INTERVAL);
  }
}

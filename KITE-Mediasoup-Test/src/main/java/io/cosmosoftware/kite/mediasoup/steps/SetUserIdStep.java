package io.cosmosoftware.kite.mediasoup.steps;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.interfaces.Runner;
import io.cosmosoftware.kite.mediasoup.pages.MediasoupPage;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.WebDriver;

public class SetUserIdStep extends TestStep {

  private final String userId;
  private final MediasoupPage mediasoupPage;
  
  public SetUserIdStep(Runner runner, String userId) {
    super(runner);
    this.userId = userId;
    this.mediasoupPage = new MediasoupPage(runner);
  }

  @Override
  public String stepDescription() {
    return "Setting Display Name to " + this.userId;
  }

  @Override
  protected void step() throws KiteTestException {
    mediasoupPage.setUserId(userId);
  }
}

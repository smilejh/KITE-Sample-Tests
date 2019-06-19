package io.cosmosoftware.kite.mediasoup.steps;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.interfaces.Runner;
import io.cosmosoftware.kite.mediasoup.pages.MediasoupPage;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.WebDriver;

public class JoinVideoCallStep extends TestStep {

   
  private final String url;
  private final MediasoupPage mediasoupPage;

  
  public JoinVideoCallStep(Runner runner, String url) {
    super(runner);
    this.url = url;
    this.mediasoupPage = new MediasoupPage(runner);
  }
  
  @Override
  public String stepDescription() {
    return "Open " + url;
  }
  
  @Override
  protected void step() throws KiteTestException {
    mediasoupPage.load(url);
  }
}
  

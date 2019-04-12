package io.cosmosoftware.kite.mediasoup.steps;

import io.cosmosoftware.kite.mediasoup.pages.MediasoupPage;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.WebDriver;

public class JoinVideoCallStep extends TestStep {

  private final MediasoupPage mediasoupPage = new MediasoupPage(this.webDriver);
  private final String url;

  
  public JoinVideoCallStep(WebDriver webDriver, String url) {
    super(webDriver);
    this.url = url;
  }
  
  @Override
  public String stepDescription() {
    return "Open " + url;
  }
  
  @Override
  protected void step() {
    mediasoupPage.load(url);
  }
}
  

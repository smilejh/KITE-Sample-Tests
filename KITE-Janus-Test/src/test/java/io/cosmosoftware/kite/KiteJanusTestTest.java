/*
 * Copyright (C) CoSMo Software Consulting Pte. Ltd. - All Rights Reserved
 */

package io.cosmosoftware.kite;

import io.cosmosoftware.kite.janus.KiteJanusTest;
import org.webrtc.kite.tests.KiteBaseTest;
import junit.framework.TestCase;
import org.openqa.selenium.WebDriver;
import org.webrtc.kite.config.EndPoint;

import javax.json.JsonObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.webrtc.kite.Utils.*;

public class KiteJanusTestTest extends TestCase {

  static {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HHmmss");
    System.setProperty("current.date", dateFormat.format(new Date()));
  }

  private static final String TEST_NAME = "Janus UnitTest";
  private static final String CONFIG_FILE = "configs/local.janus.config.json";

  private List<WebDriver> webDriverList = new ArrayList<>();
  private List<EndPoint> endPointList = getEndPointList(CONFIG_FILE, "browsers");

  private static final String platform = getSystemPlatform();

  public void setUp() throws Exception {
    super.setUp();
  }

  public void tearDown()  {
    // Close all the browsers
    for (WebDriver webDriver : this.webDriverList)
      try {
        webDriver.quit();
      } catch (Exception e) {
        e.printStackTrace();
      }
  }

  public void testTestScript() {
    KiteBaseTest test = new KiteJanusTest();
    test.setDescription(TEST_NAME);
    test.setPayload(getPayload(CONFIG_FILE, 0));
    test.setEndPointList(endPointList);
    JsonObject testResult = test.execute();
  }
}

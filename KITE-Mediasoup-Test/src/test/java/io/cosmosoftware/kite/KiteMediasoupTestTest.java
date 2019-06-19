/*
 * Copyright (C) CoSMo Software Consulting Pte. Ltd. - All Rights Reserved
 */

package io.cosmosoftware.kite;

import io.cosmosoftware.kite.mediasoup.KiteMediasoupTest;
import io.cosmosoftware.kite.report.KiteLogger;
import junit.framework.TestCase;
import org.openqa.selenium.WebDriver;
import org.webrtc.kite.config.test.Tuple;
import org.webrtc.kite.tests.KiteBaseTest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.webrtc.kite.Utils.getFirstTuple;
import static org.webrtc.kite.Utils.getPayload;

public class KiteMediasoupTestTest extends TestCase {

  static {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HHmmss");
    System.setProperty("current.date", dateFormat.format(new Date()));
  }

  //KiteLogger must be called after setting the system property "current.data"
  private final KiteLogger logger = KiteLogger.getLogger(this.getClass().getName());

  private static final String TEST_NAME = "Mediasoup UnitTest";
  private static final String CONFIG_FILE = "configs/local.mediasoup.config.json";

  private List<WebDriver> webDriverList = new ArrayList<>();
  private Tuple tuple = getFirstTuple(CONFIG_FILE);


  public void setUp() throws Exception {
    super.setUp();
  }

  public void tearDown() throws Exception {
    // Close all the browsers
    for (WebDriver webDriver : this.webDriverList)
      try {
        webDriver.quit();
      } catch (Exception e) {
        e.printStackTrace();
      }
  }

  public void testTestScript() throws Exception {
    KiteBaseTest test = new KiteMediasoupTest();
    test.setDescription(TEST_NAME);
    test.setPayload(getPayload(CONFIG_FILE, 0));
    test.setTuple(tuple);
    Object testResult = test.execute();
  }
}

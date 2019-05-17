package io.cosmosoftware.kite.mediasoup.steps;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.report.Status;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.WebDriver;

import javax.json.JsonObject;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import static io.cosmosoftware.kite.util.TestUtils.executeJsScript;
import static io.cosmosoftware.kite.util.TestUtils.waitAround;

public class StartGetStatsSDKStep extends TestStep {
    private final String pathToGetStats;
    private final String testName;
    private final String testId;
    private final String logstashUrl;
    private final String sfu;
    private final int statsPublishingInterval;
    private final String userNameCommand;
    private final String roomNameCommand;

    public StartGetStatsSDKStep(WebDriver webDriver, String testName, JsonObject getStatsSdk) {
        super(webDriver);
        this.pathToGetStats =  getStatsSdk.getString("pathToGetStatsSdk");
        this.testName = testName;
        this.testId =  getStatsSdk.getString("testId", "\"" + testName + "_"
          + new SimpleDateFormat("yyyyMMdd_hhmmss").format(new Date()) + "\"");
        this.logstashUrl = getStatsSdk.getString("logstashUrl");
        this.sfu = "\"" + getStatsSdk.getString("sfu") + "\"";
        this.statsPublishingInterval = getStatsSdk.getInt("statsPublishingInterval", 30000);
        this.userNameCommand = getStatsSdk.getString("userNameCommand");
        this.roomNameCommand = getStatsSdk.getString("roomNameCommand");
    }

    @Override
    public String stepDescription() {
        return "Loading GetStats Script for " + testId;
    }

    @Override
    protected void step() throws KiteTestException {
        logger.info("Attempting to load GetStats script");
        try {
            StringBuilder getStatsFile = Files.lines(Paths.get(pathToGetStats), 
              StandardCharsets.UTF_8).collect(StringBuilder::new, StringBuilder::append, StringBuilder::append);
            loadGetStats(getStatsFile);
            logger.info("Script loaded");
        } catch (IOException e) {
            e.printStackTrace();
            throw new KiteTestException("Failed to load GetStats", Status.BROKEN, e);
        }
        waitAround(10000);
    }

    /**
     * Load GetStats script into browser
     *
     * @param getStatsFile
     */

    private String loadGetStats (StringBuilder getStatsFile) throws KiteTestException {

        String[] initSplit = getStatsFile.toString().split("testStats.init.* pc, ");
        logger.info("Returning non-default init");
        String getStatsScript = initSplit[0] + "testStats.init(" + logstashUrl + ", " 
          + this.userNameCommand + ", " + this.roomNameCommand + ", " + sfu + ", pc, " + initSplit[1];

        String[] publishingSplit = getStatsScript.split("testStats.startPublishing\\(15000\\)");
        getStatsScript = publishingSplit[0] 
          + "testStats.startPublishing(" + statsPublishingInterval + ")" + publishingSplit[1];

        String[] pcSplit = getStatsScript.split("window\\.pc");
        getStatsScript = pcSplit[0] + "window.PC1" + pcSplit[1] + "window.PC1" + pcSplit[2];

        String[] remotePcSplit = getStatsScript.split("window\\.remotePc\\[0]");
        getStatsScript = remotePcSplit[0] + "window.PC2" + remotePcSplit[1];

        String[] remotePc2Split = getStatsScript.split("window\\.remotePc");
        getStatsScript = remotePc2Split[0] + "window.PC2" + remotePc2Split[1];

        String[] sendSplit = getStatsScript.split("KITETestName, KITETestId");
        getStatsScript = sendSplit[0];
        for (int i = 1; i <= 2; i++) {
            sendSplit[i] = "\"" + testName + "\", " + testId + sendSplit[i];
            getStatsScript = getStatsScript + sendSplit[i];
        }

        logger.info("String ready, executing Javascript script" + getStatsScript);
        return (String) executeJsScript(webDriver, getStatsScript);
    }
}
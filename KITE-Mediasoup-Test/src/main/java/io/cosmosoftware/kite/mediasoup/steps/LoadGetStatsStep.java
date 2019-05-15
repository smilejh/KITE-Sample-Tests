package io.cosmosoftware.kite.mediasoup.steps;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.report.Status;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.WebDriver;

import static io.cosmosoftware.kite.util.TestUtils.executeJsScript;
import static io.cosmosoftware.kite.util.TestUtils.waitAround;

public class LoadGetStatsStep extends TestStep {
    private final String pathToGetStats;
    private final String testName;
    private final String testId;
    private final String logstashUrl;
    private final String sfu;
    private final int statsPublishingInterval;
    private final String username = "window.CC._displayName";
    private final String room = "window.location.search.split(\"?roomId=\")[1]";

    public LoadGetStatsStep(WebDriver webDriver, String testName, String testId, String logstashUrl, String sfu, int statsPublishingInterval, String pathToGetStats) {
        super(webDriver);
        this.pathToGetStats = pathToGetStats;
        this.testName = testName;
        this.testId = testId;
        this.logstashUrl = logstashUrl;
        this.sfu = sfu;
        this.statsPublishingInterval = statsPublishingInterval;
    }

    @Override
    public String stepDescription() {
        return "Loading GetStats Script for " + testId;
    }

    @Override
    protected void step() throws KiteTestException {
        logger.info("Attempting to load GetStats script");
        try {
            StringBuilder getStatsFile = Files.lines(Paths.get(pathToGetStats), StandardCharsets.UTF_8).collect(StringBuilder::new, StringBuilder::append, StringBuilder::append);

            loadGetStats(getStatsFile, testName, testId, logstashUrl, sfu, statsPublishingInterval, username, room);
            System.out.println("Script loaded");
        } catch (IOException e) {
            e.printStackTrace();
            throw new KiteTestException("Failed to load GetStats", Status.BROKEN, e);
        }


        waitAround(30000);
    }

    /**
     * Load GetStats script into browser
     *
     * @param getStatsFile
     * @param testName
     * @param testId
     * @param logstashUrl
     * @param sfu
     * @param statsPublishingInterval
     */

    public String loadGetStats (StringBuilder getStatsFile, String testName, String testId, String logstashUrl, String sfu, int statsPublishingInterval, String username, String room) throws KiteTestException {

        String[] initSplit = getStatsFile.toString().split("testStats.init.* pc, ");
        System.out.println("Returning non-default init");
        String getStatsScript = initSplit[0] + "testStats.init(" + logstashUrl + ", " + username + ", " + room + ", " + sfu + ", pc, " + initSplit[1];

        String[] publishingSplit = getStatsScript.split("testStats.startPublishing\\(15000\\)");
        getStatsScript = publishingSplit[0] + "testStats.startPublishing(" + statsPublishingInterval + ")" + publishingSplit[1];

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

        System.out.println("String ready, executing Javascript script" + getStatsScript);
        return (String) executeJsScript(webDriver, getStatsScript);
    }
}
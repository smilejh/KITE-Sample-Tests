package io.cosmosoftware.kite.janus.steps;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.report.Status;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.cosmosoftware.kite.util.TestUtils.executeJsScript;
import static io.cosmosoftware.kite.util.TestUtils.waitAround;



public class LoadGetStatsStep extends TestStep {


    private final String pathToGetStats;
    private final String testName;
    private final String testId;
    private final String logstashUrl;
    private final String sfu;
    private final int statsPublishingInterval;

    public LoadGetStatsStep(WebDriver webDriver, String testName, String testId, String logstashUrl,
                                String sfu, int statsPublishingInterval, String pathToGetStats) {
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
        logger.info("Loading GetStats script from " + pathToGetStats);
        try {
            StringBuilder getStatsFile = Files.lines(Paths.get(pathToGetStats), StandardCharsets.UTF_8)
              .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append);
            loadGetStats(getStatsFile, testName, testId, logstashUrl, sfu, statsPublishingInterval);
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
     * 
     * @return the javascript return string
     */
    private String loadGetStats(StringBuilder getStatsFile, String testName, String testId, String logstashUrl, 
                                     String sfu, int statsPublishingInterval) throws KiteTestException {
        String[] sendSplit = getStatsFile.toString().split("KITETestName, KITETestId");
        sendSplit[1] = "\"" + testName + "\", " + testId + sendSplit[1];
        sendSplit[2] = "\"" + testName + "\", " + testId + sendSplit[2];
        String getStatsScript = sendSplit[0] + sendSplit[1] + sendSplit[2];

        String[] initSplit = getStatsScript.split("testStats.init.* pc, ");
        getStatsScript = initSplit[0] + "testStats.init(" + logstashUrl + ", " + "username, myroom, " + sfu + ", pc, " + initSplit[1];

        String[] publishingSplit = getStatsScript.split("testStats.startPublishing\\(15000\\)");
        getStatsScript = publishingSplit[0] + "testStats.startPublishing(" + statsPublishingInterval + ")" + publishingSplit[1];

        logger.info("Executing Javascript getStatsScript: \n" + getStatsScript);
        return (String) executeJsScript(webDriver, getStatsScript);
    }

}

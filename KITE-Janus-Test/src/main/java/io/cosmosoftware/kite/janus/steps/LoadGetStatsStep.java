package io.cosmosoftware.kite.janus.steps;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.janus.pages.JanusPage;
import io.cosmosoftware.kite.report.Status;
import io.cosmosoftware.kite.steps.TestStep;
import org.openqa.selenium.WebDriver;
import static io.cosmosoftware.kite.util.TestUtils.waitAround;



public class LoadGetStatsStep extends TestStep {


    private final String pathToGetStats;
    private final String testName;
    private final String testId;
    private final String logstashUrl;
    private final String sfu;
    private final boolean defaultStatsConfig;
    private JanusPage janusPage;

    public LoadGetStatsStep(WebDriver webDriver, String testName, String testId, String logstashUrl, String sfu, boolean defaultStatsConfig, String pathToGetStats) {
        super(webDriver);
        this.pathToGetStats = pathToGetStats;
        this.testName = testName;
        this.testId = testId;
        this.logstashUrl = logstashUrl;
        this.sfu = sfu;
        this.defaultStatsConfig = defaultStatsConfig;
        janusPage = new JanusPage(webDriver, logger);
    }

    @Override
    public String stepDescription() {
        return "Loading GetStats Script";
    }

    @Override
    protected void step() throws KiteTestException {
        logger.info("Attempting to load GetStats script");
        try {
            StringBuilder getStatsFile = Files.lines(Paths.get(pathToGetStats), StandardCharsets.UTF_8).collect(StringBuilder::new, StringBuilder::append, StringBuilder::append);
            //System.out.println(getStatsFile);

            janusPage.loadGetStats(getStatsFile, testName, testId, logstashUrl, sfu, defaultStatsConfig);
            System.out.println("Script loaded");
        } catch (IOException e) {
            e.printStackTrace();
            throw new KiteTestException("Failed to load GetStats", Status.BROKEN, e);
        }


        waitAround(30000);
    }
}

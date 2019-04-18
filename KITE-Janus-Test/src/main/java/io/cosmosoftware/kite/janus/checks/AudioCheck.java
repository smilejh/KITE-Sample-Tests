package io.cosmosoftware.kite.janus.checks;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.report.Reporter;
import io.cosmosoftware.kite.report.Status;
import io.cosmosoftware.kite.steps.TestStep;
import io.cosmosoftware.kite.util.TestUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.webrtc.kite.Utils;
import org.webrtc.kite.config.Browser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

import static io.cosmosoftware.kite.entities.Timeouts.TEN_SECOND_INTERVAL;

public class AudioCheck extends TestStep {

  private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());

  private final int _128_BITS = 128;

  private Map<String, Object> sessionMap;

  private final String scoreDirectory;

  private final String scoreCommand;

  private final String audioDuration;

  public AudioCheck(WebDriver webDriver, Map<String, Object> sessionMap, String scoreDirectory, String scoreCommand,
                    String audioDuration) {
    super(webDriver);
    this.sessionMap = sessionMap;
    this.scoreDirectory = scoreDirectory;
    this.scoreCommand = scoreCommand;
    this.audioDuration = audioDuration;
  }

  @Override
  public String stepDescription() {
    return "Verify the recorded audio of the receiver";
  }

  @Override
  protected void step() throws KiteTestException {
    String nodeHost = sessionMap.get("node_host") + "/extra/AudioRecorder?";
    String response = TestUtils.doHttpGet(nodeHost + "record=1&duration=" + this.audioDuration);
    logger.info("Recording Response: " + response);
    logger.info("Waiting for recording to finish ...");
    TestUtils.waitAround((Utils.getSeconds(this.audioDuration) * 1000) + TEN_SECOND_INTERVAL);
    try {
      String filePath = this.scoreDirectory + File.separator + "record.wav";
      TestUtils.downloadFile(nodeHost + "download=1", filePath);
      if (isZeroLength(filePath)) {
        throw new KiteTestException("No audio was received at the other end", Status.FAILED);
      } else {
        String[] command = {
          this.scoreCommand,
          "+8000",
          ((Browser) this.sessionMap.get("end_point")).getFakeMediaAudio(),
          filePath
        };
        response = TestUtils.executeCommand( this.scoreDirectory, Arrays.asList(command), logger, "AudioCheck");
        logger.info("Score Response: " + response);
        Reporter.getInstance().textAttachment(this.report, "AudioQualityScore", response, "text");
      }
    } catch (Exception e) {
      logger.warn("Exception in AudioCheck Step", e);
      throw new KiteTestException(e.getMessage(), Status.BROKEN, e, true);
    }
  }

  /**
   * Verify the size of the recorded file.
   *
   * @param pathToFile path to recorded file.
   * @return true if the recorded file is empty.
   * @throws IOException the IOException.
   */
  private boolean isZeroLength(String pathToFile) throws IOException {
    FileInputStream fileInputStream = null;
    long duration = 0;
    try {
      fileInputStream = new FileInputStream(pathToFile);
      duration = Objects.requireNonNull(fileInputStream).getChannel().size() / _128_BITS;
    } finally {
      if (fileInputStream != null) {
        try {
          fileInputStream.close();
        } catch (IOException e) {
          // ignore
        }
      }
    }
    return duration == 0;
  }
}

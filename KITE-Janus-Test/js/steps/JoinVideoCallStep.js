const {TestUtils, TestStep} = require('kite-common');
const {janusPage} = require('../pages');
/**
 * Class: JoinVideoCallStep
 * Extends: TestStep
 * Description:
 */
class JoinVideoCallStep extends TestStep {
  constructor(driver, url, numberOfparticipant,  timeout) {
    super();
    this.driver = driver;
    this.url = url;
    this.numberOfparticipant= numberOfparticipant;
    this.timeout = timeout;
  }

  stepDescription() {
    return 'Open ' + this.url + ' and check the video';
  }

  async step(allureTestReport, reporter) {
    await janusPage.open(this.driver, this.url, this.timeout);
    let details = await janusPage.verifyVideo(this.driver, this.numberOfparticipant, this.timeout);
    reporter.textAttachment(this.report, "videoChecks", JSON.stringify(details), "json");
  }
}

module.exports = JoinVideoCallStep;
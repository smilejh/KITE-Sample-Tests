const {TestUtils, WebDriverFactory, KiteBaseTest} = require('kite-common'); 
const {LoadPageStep, GetStatsStep, ScreenshotStep, SelectProfileStep} = require('./steps');
const {SenderVideoCheck, ReceivedVideoCheck, GaugesCheck} = require('./checks');

// KiteBaseTest config
const globalVariables = TestUtils.getGlobalVariables(process);
const capabilities = require(globalVariables.capabilitiesPath);
const payload = require(globalVariables.payloadPath);


const rids = ['a', 'b', 'c'];
const tids = [0, 1, 2];


class Medooze extends KiteBaseTest {
  constructor(name, globalVariables, capabilities, payload) {
    super(name, globalVariables, capabilities, payload);
  }
  
  async testScript() {
    try {
      this.driver = await WebDriverFactory.getDriver(capabilities, capabilities.remoteAddress);

      let loadPageStep = new LoadPageStep(this);
      await loadPageStep.execute(this);

      // wait for video
      await TestUtils.waitAround(5000);
      
      let senderVideoCheck = new SenderVideoCheck(this);
      await senderVideoCheck.execute(this);

      let receivedVideoCheck = new ReceivedVideoCheck(this);
      await receivedVideoCheck.execute(this);

      await TestUtils.waitAround(5000);
      let getStatsStep = new GetStatsStep(this, 'pc');
      await getStatsStep.execute(this);

      let screenshotStep = new ScreenshotStep(this);
      await screenshotStep.execute(this);

      for(let i = 0; i < rids.length; i++) {
        for(let j = 0; j < tids.length; j++) {
          let selectProfileStep = new SelectProfileStep(this, rids[i], tids[j]);
          await selectProfileStep.execute(this);
          let gaugesCheck = new GaugesCheck(this, rids[i], tids[j]);
          await gaugesCheck.execute(this);
        }
      }

      this.report.setStopTimestamp();
    } catch (e) {
      console.log(e);
    } finally {
      await this.driver.quit();
    }
    this.reporter.generateReportFiles();
    let value = this.report.getJsonBuilder();
    TestUtils.writeToFile(this.reportPath + "/result.json", JSON.stringify(value));
  }
}

module.exports= Medooze;

let test = new Medooze('Medooze test', globalVariables, capabilities, payload);
test.testScript();

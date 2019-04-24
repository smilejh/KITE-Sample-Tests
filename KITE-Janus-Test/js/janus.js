const {TestUtils, WebDriverFactory, KiteBaseTest} = require('kite-common');
const waitAround = TestUtils.waitAround;
const globalVariables = TestUtils.getGlobalVariables(process);
const {JoinVideoCallStep, GetStatsStep} = require('./steps'); 

const numberOfParticipant = globalVariables.numberOfParticipant;
const id = globalVariables.id;
const capabilities = require(globalVariables.capabilitiesPath);
const payload = require(globalVariables.payloadPath);
const reportPath = globalVariables.reportPath;




function getRoomUrl() {
  const roomid = Math.floor(id / payload.usersPerRoom);
  if (roomid > payload.rooms.length) {
    console.error('Not enough rooms');
    return;
  }
  return payload.url + payload.rooms[roomid] + '&username=user' + Array(Math.max(3 - String(id).length + 1, 0)).join(0) + id;
}


class Janus extends KiteBaseTest {
  constructor(name, payload, reportPath) {
    super(name, payload, reportPath);
  }

  async testScript() {
    try {
      var driver = await WebDriverFactory.getDriver(capabilities, capabilities.remoteAddress);
      let joinVideoCallStep = new JoinVideoCallStep(driver, getRoomUrl(), numberOfParticipant, this.timeout);
      await joinVideoCallStep.execute(this.report, this.reporter);
      let pcArray = [];
      pcArray.push('window.pc');
      for(let i = 0; i < numberOfParticipant -1; i++) {
        pcArray.push('window.remotePc[' + i + ']');
      }
      let getStatsStep = new GetStatsStep(driver, this.statsCollectionDuration, this.statsCollectionInterval, pcArray);
      await getStatsStep.execute(this.report, this.reporter);
      
      this.report.setStopTimestamp();
    } catch (e) {
      console.log(e);
    } finally {
      await driver.quit();
    }
    this.reporter.generateReportFiles();
    let value = this.report.getJsonBuilder();
    TestUtils.writeToFile(this.reportPath + "/result.json", JSON.stringify(value));
  }
}

module.exports = Janus;

var test = new Janus('Janus test', payload, reportPath);  
test.testScript();





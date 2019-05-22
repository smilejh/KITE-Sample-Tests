const {TestUtils, WebDriverFactory, KiteBaseTest} = require('kite-common');
const {JoinVideoCallStep, ScreenshotStep, GetStatsStep} = require('./steps'); 
const {FirstVideoCheck, AllVideoCheck} = require('./checks');

const globalVariables = TestUtils.getGlobalVariables(process);

// KiteBaseTest config
const capabilities = require(globalVariables.capabilitiesPath);
const payload = require(globalVariables.payloadPath);

function getRoomUrl(id) {
  const roomid = Math.floor(id / payload.usersPerRoom);
  if (roomid > payload.rooms.length) {
    console.error('Not enough rooms');
    return;
  }
  return payload.url + payload.rooms[roomid] + '&username=user' + Array(Math.max(3 - String(id).length + 1, 0)).join(0) + id;
}

function getPcArray(numberOfParticipant) {
  let pcArray = [];
  for(let i = 0; i < numberOfParticipant-1; i++) {
    pcArray.push('window.remotePc[' + i + ']');
  }
  return pcArray;
}

class Janus extends KiteBaseTest{
  constructor(name, globalVariables, capabilities, payload) {
    super(name, globalVariables, capabilities, payload);
  }

  async testScript() {
    try {
      this.driver = await WebDriverFactory.getDriver(this.capabilities, this.capabilities.remoteAddress);

      let joinVideoCallStep = new JoinVideoCallStep(this, getRoomUrl(this.id));
      await joinVideoCallStep.execute(this);

      let firstVideoCheck = new FirstVideoCheck(this);
      await firstVideoCheck.execute(this);

      let allVideoCheck = new AllVideoCheck(this);
      await allVideoCheck.execute(this);

      let pcArray = getPcArray(this.numberOfParticipant);
      let getStatsStep = new GetStatsStep(this, pcArray);
      await getStatsStep.execute(this);
      
      let screenshotStep = new ScreenshotStep(this);
      await screenshotStep.execute(this);
      
      await TestUtils.waitAround(3000 * this.numberOfParticipant); // 3s per participant
    } catch (e) {
      console.log(e);
    } finally {
      this.driver.quit();
    }
  }
}

module.exports = Janus;

let test = new Janus('Janus test', globalVariables, capabilities, payload); 

test.run();

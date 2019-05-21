const {TestUtils, WebDriverFactory, KiteBaseTest} = require('kite-common');
const {JoinUrlStep, ScreenshotStep, GetStatsStep} = require('./steps'); 
const {FirstVideoCheck, AllVideoCheck} = require('./checks');
const {JanusPage} = require('./pages');

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

function getPcArray(numberOfParticipant, peerConnections) {
  for(let i = 0; i < numberOfParticipant-1; i++) {
    peerConnections.push('window.remotePc[' + i + ']');
  }
  return peerConnections;
}

class Janus extends KiteBaseTest{
  constructor(name, globalVariables, capabilities, payload) {
    super(name, globalVariables, capabilities, payload);
    this.url = getRoomUrl(this.id);
    // Modif
    this.page = new JanusPage();
  }

  async testScript() {
    try {
      this.driver = await WebDriverFactory.getDriver(this.capabilities, this.capabilities.remoteAddress);

      let joinUrlStep = new JoinUrlStep(this);
      await joinUrlStep.execute(this);

      let firstVideoCheck = new FirstVideoCheck(this);
      await firstVideoCheck.execute(this);

      let allVideoCheck = new AllVideoCheck(this);
      await allVideoCheck.execute(this);

      let peerConnections = [];
      peerConnections.push("window.pc");

      this.peerConnections = getPcArray(this.numberOfParticipant, peerConnections);
      let getStatsStep = new GetStatsStep(this);
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

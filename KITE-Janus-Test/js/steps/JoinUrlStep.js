const {TestStep} = require('kite-common');
const {JanusEchoPage, 
      JanusStreamingPage, JanusVideoCallPage, 
      JanusVideoRoomPage} = require('../pages');

const description = function(stepInfo) {
  if (stepInfo.page instanceof JanusEchoPage || stepInfo.page instanceof JanusStreamingPage
    || stepInfo.page instanceof JanusVideoCallPage) {
    return 'Open ' + stepInfo.url + ' and join a session';
  }
  if (stepInfo.page instanceof JanusVideoRoomPage) {
    return 'Open ' + stepInfo.url + ' and join the room';
  }
}

const executeStep = async function(stepInfo) {
  if (stepInfo.page instanceof JanusEchoPage || stepInfo.page instanceof JanusStreamingPage) {
    await stepInfo.page.open(stepInfo);
    await stepInfo.page.joinSession(stepInfo);
  }
  if (stepInfo.page instanceof JanusVideoCallPage) {
    await stepInfo.page.open(stepInfo);
    await stepInfo.page.joinSession(stepInfo, 'VideoCall');
  }
  if (stepInfo.page instanceof JanusVideoRoomPage) {
    await stepInfo.page.open(stepInfo);
    await stepInfo.page.joinSession(stepInfo, 'VideoRoom');
  }
}

class JoinUrlStep extends TestStep {
  constructor(kiteBaseTest) {
    super();
    this.driver = kiteBaseTest.driver;
    this.url = kiteBaseTest.url;
    this.timeout = kiteBaseTest.timeout;
    this.page = kiteBaseTest.page;
    this.id = kiteBaseTest.id;
    this.uuid = kiteBaseTest.uuid;
  }

  stepDescription() {
    return description(this);
  }

  async step() {
    await executeStep(this);
  }
}

module.exports = JoinUrlStep;
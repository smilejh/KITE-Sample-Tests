const {By, Key} = require('selenium-webdriver');
const {TestUtils, KiteTestError, Status} = require('kite-common'); 
const waitAround = TestUtils.waitAround;
const verifyVideoDisplayByIndex = TestUtils.verifyVideoDisplayByIndex;

const meetingRoom = By.id('enter_room_field');
const videoElements = By.css('video');


const getPeerConnectionScript = function() {
  return "window.pc = [];"
    + "map = APP.conference._room.rtc.peerConnections;"
    + "for(var key of map.keys()){"
    + "  window.pc.push(map.get(key).peerconnection);"
    + "}";
}

class JitsiPage {
  constructor() {}

  async open(stepInfo) {
    await TestUtils.open(stepInfo);
  }

  async enterRoom(stepInfo, room) {
    const roomId = room + stepInfo.uuid;
    let meeting = await stepInfo.driver.findElement(meetingRoom);
    await meeting.sendKeys(roomId); // Fill out the field and add some random numbers
    await meeting.sendKeys(Key.ENTER); // Press ENTER to enter in the room
  }

  // VideoCheck with verifyVideoDisplayByIndex
  async videoCheck(stepInfo, index) {
    let videos = []; // it will contains our video elements
    let checked; // Result of the verification
    let timeout = stepInfo;
    stepInfo.numberOfParticipant = parseInt(stepInfo.numberOfParticipant) + 1; // To avoid the first video
    
    // Waiting for all the videos
    let i = await TestUtils.waitVideos(stepInfo, videoElements);

    // Make sure that it has not timed out
    if (i === timeout) {
      throw new KiteTestError(Status.FAILED, "unable to find " +
        stepInfo.numberOfParticipant + " <video> element on the page. Number of video found = " +
        videos.length);
    }
    // Check the status of the video
    // checked.result = 'blank' || 'still' || 'video'
    i = 0;
    checked = await verifyVideoDisplayByIndex(stepInfo.driver, index + 1);
    while(checked.result === 'blank' || checked.result === undefined && i < timeout) {
      checked = await verifyVideoDisplayByIndex(stepInfo.driver, index + 1);
      i++;
      await waitAround(1000);
    }

    i = 0;
    while(i < 3 && checked.result != 'video') {
      checked = await verifyVideoDisplayByIndex(stepInfo.driver, index + 1);
      i++;
      await waitAround(3 * 1000); // waiting 3s after each iteration
    }
    return checked.result;
  }

  // GetStats for jitsi
  // Todo: doc
  async getStats(stepInfo) {
    await stepInfo.driver.executeScript(getPeerConnectionScript());
    let stats = await TestUtils.getStats(stepInfo, 'kite', stepInfo.peerConnections[0]);
    return stats;
  }
}

module.exports = JitsiPage;
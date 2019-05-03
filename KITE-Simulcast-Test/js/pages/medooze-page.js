const {By, Key, promise} = require('selenium-webdriver');
const map = promise.map;
const {TestUtils, Status, KiteTestError} = require('kite-common'); 
const waitAround = TestUtils.waitAround;

const elements = {
  peervideo: By.id('peervideo'),
  myvideo: By.id('myvideo'),
  curres: By.id('curres'),
  high: By.id('high'),
  medium: By.id('medium'),
  low: By.id('low'),

  video: {
    type: 'tagName',
    value: 'video',
  },

  videos: By.css('video'),
}

const buttons = {
  sl0Button: By.id('sl-0'),
  sl1Button: By.id('sl-1'),
  sl2Button: By.id('sl-2'),
  tl0Button: By.id('tl-0'),
  tl1Button: By.id('tl-1'),
  tl2Button: By.id('tl-2'),
};

module.exports = { 

  // Click the start button
  clickStartButton: async function(driver) {
    let startButton = await driver.findElement(buttons.startButton);
    await startButton.click();
  },

  clickButton: async function(driver, rid, tid) {
    let button;
    switch (rid) {
      case 'a':
      button = await driver.findElement(sl2Button);
      break;
      case 'b':
        button = await driver.findElement(sl2Button);
        break;
      case 'c':
        button = await driver.findElement(sl2Button);
        break;
    }
    if (button != undefined) {
      await button.click();
    }
    switch (tid) {
      case 0:
        button = await driver.findElement(tl0Button);
        break;
      case 1:
        button = await driver.findElement(tl1Button);
        break;
      case 2:
        button = await driver.findElement(tl2Button);
        break;
      default:
        break;
    }
    if (button != undefined) {
      await button.click();
    }
  },


  // type: width / height / fps / bandwidth 
  // idx: 0 for sent stats / 1 for received stats
  loopbackStats: async function(driver, type, idx) {
    let script = "return s_s_";
    switch(type) {
      case "width":
        script += 'w[';
        break;
      case "height":
        script += 'h[';
        break;
      case "fps":
        script += 'f[';
        break;
      case "bandwidth":
        script += 'b[';    
        break;
      default:
        return 0;
    }
    switch(idx) {
      case 0:
        script += 0;
        break;
      case 1:
        script += 1;
        break;
      default:
        return 0;
    }
    script += '].innerText;'
    return await driver.executeScript(script);
  },

  getLoopbackStats: async function(stepInfo) {
    let loopBackStats = {};

    loopBackStats.sentWidth = await this.loopbackStats(stepInfo.driver,"width", 0);
    loopBackStats.sentHeight = await this.loopbackStats(stepInfo.driver,"height", 0);
    loopBackStats.sentFPS = await this.loopbackStats(stepInfo.driver,"fps", 0);
    loopBackStats.sentBW = await this.loopbackStats(stepInfo.driver,"bandwidth", 0);
    loopBackStats.recvWidth = await this.loopbackStats(stepInfo.driver,"width", 1);
    loopBackStats.recvHeight = await this.loopbackStats(stepInfo.driver,"height", 1);
    loopBackStats.recvFPS = await this.loopbackStats(stepInfo.driver,"fps", 1);
    loopBackStats.recvBW = await this.loopbackStats(stepInfo.driver, "bandwidth", 1);

    return loopBackStats;
  },

  // SelectProfileStep
  selectProfile: async function(stepInfo) {
    const button = await stepInfo.driver.findElement(By.xpath('//button[@data-rid="' + stepInfo.rid + '" and @data-tid="' + stepInfo.tid + '"]'));
    button.sendKeys(Key.ENTER);
    await TestUtils.waitAround(stepInfo.statsCollectionInterval);
  },

  // VideoCheck
  videoCheck: async function(stepInfo, direction) {
    //wait a while to allow the video to load.
    await TestUtils.waitAround(2000);
    let videos;
    let ids = [];
    let i = 0;
    await TestUtils.waitForElement(stepInfo.driver, elements.video.type, elements.video.value, stepInfo.timeout);

    while (ids.length < stepInfo.numberOfParticipant && i < stepInfo.timeout / 1000) {
      videos = await stepInfo.driver.findElements(elements.videos);
      ids = await map(videos, e => e.getAttribute("id"));
      i++;
      await waitAround(1000);
    }

    if (i === stepInfo.timeout) {
      throw new KiteTestError(Status.FAILED, "Unable to find " + stepInfo.numberOfParticipants + " <video> element on the page. No video found = " + ids.length);
    }

    let idx = ("sent" === direction) ? 0 : 1;
    let checked = await TestUtils.verifyVideoDisplayById(stepInfo.driver, ids[1]);
    i = 0;
    while(i < 3 && checked.result != 'video') {
      checked = await TestUtils.verifyVideoDisplayById(stepInfo.driver, ids[1]);
      i++;
      await waitAround(3 * 1000);
    }
    return checked.result;
  },

  // Bandwidth check
  /* Set the bitrate cap (in bps)
  setBitrateCap: async function(driver, str) {
    let script = "$('#cap').html('" + str + "');echotest.send({message: {bitrate:" + str + "}})"; 
    await driver.executeScript(script);
    console.log("bitrate cap set to " + str + "bps");
  },

  
  lowHigherThanMedium: async function(driver) {
    let l = await parseInt(driver.findElement(elements.low).innerText);
    let m = await parseInt(driver.findElement(elements.medium).innerText);
    return l != 0 && m != 0 && (l > m);
  }

  lowHigherThanMedium: async function(driver) {
    let m = await parseInt(driver.findElement(elements.medium).innerText);
    let h = await parseInt(driver.findElement(elements.high).innerText);
    return h != 0 && m != 0 && (m > h);
  }
  */
}

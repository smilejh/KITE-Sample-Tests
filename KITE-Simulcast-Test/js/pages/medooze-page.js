const {By, Key, promise} = require('selenium-webdriver');
const map = promise.map;
const {TestUtils, Status, KiteTestError} = require('kite-common'); 
const waitAround = TestUtils.waitAround;
const verifyVideoDisplayById = TestUtils.verifyVideoDisplayById;

const elements = {
  peervideo: By.id('peervideo'),
  myvideo: By.id('myvideo'),
  curres: By.id('curres'),
  high: By.id('high'),
  medium: By.id('medium'),
  low: By.id('low'),

  videos: By.css('video'),
}

const statsWidth = By.id("s_s_w");
const statsHeight = By.id("s_s_h");
const statsFps = By.id("s_s_f");
const statsBandwidth = By.id("s_s_b");

const buttons = {
  sl0Button: By.id('sl-0'),
  sl1Button: By.id('sl-1'),
  sl2Button: By.id('sl-2'),
  tl0Button: By.id('tl-0'),
  tl1Button: By.id('tl-1'),
  tl2Button: By.id('tl-2'),
};

module.exports = { 

  open: async function(stepInfo) {
    await TestUtils.open(stepInfo);
  },

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
    let element;
    switch(type) {
      case "width":
        element = await driver.findElements(statsWidth);
        break;
      case "height":
        element = await driver.findElements(statsHeight);
        break;
      case "fps":
        element = await driver.findElements(statsFps);
        break;
      case "bandwidth":
        element = await driver.findElements(statsBandwidth);    
        break;
      default:
        return 0;
    }
    let value = await element[idx].getText(); 
    return value;
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
    await waitAround(stepInfo.statsCollectionInterval);
  },

  // VideoCheck
  videoCheck: async function(stepInfo, direction) {
    let videos;
    let ids = [];
    let i = 0;
    let timeout = stepInfo.timeout / 1000;

    while (ids.length < stepInfo.numberOfParticipant && i < timeout) {
      videos = await stepInfo.driver.findElements(elements.videos);
      ids = await map(videos, e => e.getAttribute("id"));
      i++;
      await waitAround(1000);
    }

    if (i === timeout) {
      throw new KiteTestError(Status.FAILED, "Unable to find " + stepInfo.numberOfParticipant + " <video> element on the page. Number of video found = " + ids.length);
    }

    let idx = ("sent" === direction) ? 0 : 1;
    let checked = await verifyVideoDisplayById(stepInfo.driver, ids[idx]);
    i = 0;
    checked = await verifyVideoDisplayById(stepInfo.driver, ids[idx]);
    while(checked.result === 'blank' || checked.result === undefined && i < timeout) {
      checked = await verifyVideoDisplayById(stepInfo.driver, ids[idx]);
      i++;
      await waitAround(1000);
    }

    i = 0;
    while(i < 3 && checked.result != 'video') {
      checked = await verifyVideoDisplayById(stepInfo.driver, ids[idx]);
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

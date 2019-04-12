const {TestUtils, WebDriverFactory} = require('kite-common');
const waitAround = TestUtils.waitAround;
const {LoadPageStep, GetStatsStep, StayInMeetingStep, TakeScreenshotStep, SelectProfileStep, GetGaugeStatStep} = require('./steps');
const {CheckVideoStep} = require('./checks');
const {Builder, By, Key, until, promise} = require('selenium-webdriver');

// Global variables:

// Get params form command line
const numberOfParticipant = process.argv[2];
const id = process.argv[3];
const reportPath = process.argv[4];
// Bind console log (need more details)
/*console.logCopy = console.log.bind(console);
console.log = function(data) {
    var timestamp = '[' + new Date().toUTCString() + '] ';
    this.logCopy(timestamp, data);
};*/
// Create correct path to files
const resultFilePath = reportPath + '/' + id + '/result.json';
const capabilitiesPath = reportPath + '/' + id + '/capabilities.json';
const payloadPath = reportPath + "/payload.json";
// Read objects from files
const capabilities = require(capabilitiesPath);
const payload = require(payloadPath);

const timeout = 60 * 1000; // 60s
const A_BIT = 3 * 1000; // 60s
//console.log('<<<<<<- Found id ->>>>> ' + id);
//console.log('<<<<<<- Found report path ->>>>> ' + reportPath);
//console.log('<<<<<<- Found payload ->>>>> ' + JSON.stringify(payload));
//console.log('<<<<<<- Found capabilities ->>>>> ' + JSON.stringify(capabilities));

const rids = ['a', 'b', 'c'];
const tids = [1, 2, 3];

function getRoomUrl() {
  return payload.url + '';
}

async function evaluateProfile(driver, testReport, rid, tid) {
	await SelectProfileStep.execute(driver, testReport, rid, 0); // 0 ?
	await StayInMeetingStep.execute(driver, testReport, A_BIT); // 3s seems reasonable for loading
	await GetGaugeStatStep.execute(driver, testReport, rid, 0);
	await TakeScreenshotStep.execute(driver, testReport, reportPath + '/' + id + "/screenshots", 'ScreenshotStep_'+rid+'0'+ '.png');
}


(async function testScript() {
	let testReport = {};
	let steps = [];
	testReport['status'] = 'passed';
	testReport['start'] = Date.now();
  testReport['steps'] = steps;
	let driver;
  try {
		driver = await WebDriverFactory.getDriver(capabilities, capabilities.remoteAddress)
		await LoadPageStep.execute(driver, testReport, getRoomUrl(), timeout);
		await waitAround(A_BIT);
		await CheckVideoStep.execute(driver, testReport, 'local', timeout);
		await CheckVideoStep.execute(driver, testReport, 'remote', timeout);
		await TakeScreenshotStep.execute(driver, testReport, reportPath  + '/' + id + "/screenshots", 'ScreenshotStep_' + Date.now() + '.png');
		for (let i = 0; i < rids.length; i++) {
			await SelectProfileStep.execute(driver, testReport, rids[i], 0); // 0 ?
			await GetGaugeStatStep.execute(driver, testReport, rids[i], 0);
			await TakeScreenshotStep.execute(driver, testReport, reportPath + '/' + id + "/screenshots", 'ScreenshotStep_'+rids[i]+'-0'+ '.png');
		}
  } catch(e) {
    console.error('CAUGHT YA: ' +  e);
  } finally {
    await driver.quit();
  }
  testReport['stop'] = Date.now();
  TestUtils.writeToFile(resultFilePath,JSON.stringify(testReport));
  console.log('FINISHED');
})();
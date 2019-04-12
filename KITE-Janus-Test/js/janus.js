const {TestUtils, WebDriverFactory} = require('kite-common');
const waitAround = TestUtils.waitAround;
const {JoinVideoCallStep, GetStatsStep} = require('./steps');
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

const timeout = payload.testTimeout * 1000;
//console.log('<<<<<<- Found id ->>>>> ' + id);
//console.log('<<<<<<- Found report path ->>>>> ' + reportPath);
//console.log('<<<<<<- Found payload ->>>>> ' + JSON.stringify(payload));
//console.log('<<<<<<- Found capabilities ->>>>> ' + JSON.stringify(capabilities));

// Purge module from node's cache (not sure why)
//TestUtils.purgeCache(capabilitiesPath);
//TestUtils.purgeCache(payloadPath);


function getRoomUrl() {
  const roomid = Math.floor(id / payload.usersPerRoom);
  if (roomid > payload.rooms.length) {
    console.error('Not enough rooms');
    return;
  }
  return payload.url + payload.rooms[roomid] + '&username=user' + Array(Math.max(3 - String(id).length + 1, 0)).join(0) + id;
}


(async function testScript() {
  let driver;

  // Test report
	let testReport = {};
	let steps = [];
	testReport['status'] = 'passed';
  testReport['steps'] = steps;
	// todo: address comes from file

  // beginning of tests
  testReport['start'] = Date.now();
  try {
		driver = await WebDriverFactory.getDriver(capabilities, capabilities.remoteAddress)
		await JoinVideoCallStep.execute(driver, testReport, getRoomUrl(), timeout);
		const pcArray = [];
		pcArray.push('window.pc'); // sent stats
		for (let i = 0; i < numberOfParticipant -1 ; i++) {
			pcArray.push('window.remotePc[' + i + ']');
		}
		await GetStatsStep.execute(driver, testReport, pcArray);
    await waitAround(5000);
    
  } catch(e) {
    console.error(e);

  } finally {
    await driver.quit();
  }
  // End of test report
  testReport['stop'] = Date.now();
  TestUtils.writeToFile(resultFilePath,JSON.stringify(testReport));
})();
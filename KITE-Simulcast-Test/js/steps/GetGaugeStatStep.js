const {Builder, By, Key, until, promise} = require('selenium-webdriver');
const {TestUtils} = require('kite-common');
const waitAround = TestUtils.waitAround;

const map = promise.map;

module.exports = {
	execute: async function(driver, testReport, rid, tid) {
		let report = {};
		let result = 'passed';
		let attachment = {};
		attachment['type'] = 'json';
    report['name'] = 'Get the reading on gauge :' +  rid + ';' + tid;
    report['start'] = Date.now();
    if (testReport.status === 'passed') {
			try {
        console.log('executing: ' + report.name);
        await waitAround(3000);
				const loopBackStats = {};
		    loopBackStats.sentWidth = await driver.executeScript('return s_s_w[0].innerText;');
        loopBackStats.sentHeight = await driver.executeScript('return s_s_h[0].innerText;');;
        loopBackStats.sentFPS = await driver.executeScript('return s_s_f[0].innerText;');;
        loopBackStats.sentBW = await driver.executeScript('return s_s_b[0].innerText;');;
        loopBackStats.recvWidth = await driver.executeScript('return s_s_w[1].innerText;');;
        loopBackStats.recvHeight = await driver.executeScript('return s_s_h[1].innerText;');;
        loopBackStats.recvFPS = await driver.executeScript('return s_s_f[1].innerText;');;
        loopBackStats.recvBW = await driver.executeScript('return s_s_b[1].innerText;');
        attachment['value'] = loopBackStats;
      } catch (error) {
        result = 'failed';
        testReport['status'] = result;
        attachment['value'] = '' + error;
        console.log(error);
      }
    } else {
      result = 'skipped';
      console.log('skipping: ' + report.name);
    }
    report['attachment'] = attachment;
    report['status'] = result;
    report['stop'] = Date.now();
    testReport.steps.push(report);
	}
}
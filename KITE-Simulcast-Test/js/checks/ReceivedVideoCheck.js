const VideoCheck = require('./VideoCheck');

/**
 * Class: ReceivedVideoCheck
 * Extends: VideoCheck
 * Description:
 */
class ReceivedVideoCheck extends VideoCheck {
  constructor(kiteBaseTest) {
    super(kiteBaseTest);
  }

  stepDescription() {
    return "Check the first video is being received OK";
  }

  async step() {
    let direction = 'received';
    super.step(direction);
  }
}

module.exports = ReceivedVideoCheck;
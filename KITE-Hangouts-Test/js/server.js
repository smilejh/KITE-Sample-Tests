const app = require('express')();
const http = require('http').createServer(app);
const io = require('socket.io')(http);

let stepArray = [];
let doneArray = [];
let roomUrlArray = {};

const port = process.argv[2];
const numberOfParticipant = process.argv[3];
const interval = 1000;

io.on('connection', function(socket) {
  console.log('Connected');

  socket.on('disconnect', function(){
  });

  socket.on("test finished", (id) => {
    console.log('test finished[' + id +']');
    stepArray.push(id);
  })

  socket.on("done", (id) => {
    console.log('done[' + id +']');
    doneArray.push(id);
  })
  
  socket.on("registerRoomUrl", (roomUrl, id) => {
    roomUrlArray[id] = roomUrl;
    console.log('registerRoomUrl[' + id + '] ' + roomUrlArray[id]);
  })
  
  socket.on("getRoomUrl", (id, usersPerRoom) => {
    const pubId = 0
    console.log('getRoomUrl = ' + roomUrlArray[pubId]);
    socket.emit('url', roomUrlArray[pubId]);
  })
  
  function isFinished(){
    if(stepArray.length == numberOfParticipant) {
      socket.broadcast.emit('finished');
      console.log('all finished');
    }
  }
  
  function isDone() {
    if(doneArray.length == numberOfParticipant) {
      stepArray = [];
      doneArray = [];
      console.log('all done');
    }
  }

  function ckeckStatus() {
    isFinished();
    isDone();
  }

  setInterval(ckeckStatus, interval);
});

http.listen(port, function() {
  console.log('Listening on port ' + port);
});



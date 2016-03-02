document.addEventListener('DOMContentLoaded', function () {
  // PeerJS server details
  var SERVER_IP = '10.143.107.134';
  var SERVER_PORT = 9000;

  //DOM elements
  var messages = document.querySelector('#messages');
  var clientID = document.querySelector('#client-id');
  var connectBtn = document.querySelector('#connect');
  var streamID = document.querySelector('#stream-id');
  var videoStream = document.querySelector('#video-stream');

  //Objects
  var thisID = null; //This client's ID
  var peer = null;  //PeerJS object

  // Display messages/errors
  var makePara = function (text) {
    var p = document.createElement('p');
    p.innerText = text;
    return p;
  };

  var addMessage = function (para) {
    if (messages.firstChild) {
      messages.insertBefore(para, messages.firstChild);
    }
    else {
      messages.appendChild(para);
    }
  };

  var logError = function (text) {
    var p = makePara('ERROR: ' + text);
    p.style.color = 'red';
    addMessage(p);
  };

  var logMessage = function (text) {
    addMessage(makePara(text));
  };

  // set the "REMOTE" video element source
  var showRemoteStream = function (stream) {
    videoStream.src = window.URL.createObjectURL(stream);
  };

  // set caller ID and connect to the PeerJS server
  var connect = function () {
    thisID = clientID.value;

    if (!thisID) {
      logError('Please choose client ID first.');
      return;
    }

    try {
      // create connection to the ID server
      peer = new Peer(thisID, {host: SERVER_IP, port: SERVER_PORT});

      peer.socket._socket.onclose = function () {
        logError('No connection to the server.');
        peer = null;
      };
	  
	  peer.on('open', function() {
		 logMessage('Connected to server');
	  });
	  
      //Call event to capture incoming streaming request
      peer.on('call', answer);
    }
    catch (e) {
      peer = null;
      logError('Error while connecting to the server.');
    }
  };

  // answer incoming stream request
  var answer = function (call) {
    if (!peer) {
      logError('No connection for incoming stream.');
      return;
    }

    logMessage('Viewing video stream');
	call.answer();
    call.on('stream', showRemoteStream);
	
  };

  // wire up button events
  connectBtn.addEventListener('click', connect);
});
document.addEventListener('DOMContentLoaded', function () {
  // PeerJS server location
  var SERVER_IP = '10.143.107.134';
  var SERVER_PORT = 9000;

  //DOM elements

  var messages = document.querySelector('#feedback-messages');
  var clientID = document.querySelector('#client-id');
  var connectBtn = document.querySelector('#connect');
  var remoteVideo = document.querySelector('#remote-video-stream');
  var localVideo = document.querySelector('#my-local-stream');
  var streamID = document.querySelector('#stream-id');
  var streamBtn = document.querySelector('#stream-button');
  
  //Objects
  var thisID = null; //This client's ID
  var peer = null;  //PeerJS object
  var localStream = null;  //Local video stream



  //Get the local video stream
  var getLocalStream = function (successCb) {
    if (localStream && successCb) {
      successCb(localStream);
    }
    else {
      navigator.webkitGetUserMedia(
        {
          audio: true,
          video: true
        },

        function (stream) {
          localStream = stream;

          localVideo.src = window.URL.createObjectURL(stream);

          if (successCb) {
            successCb(stream);
          }
        },

        function (err) {
          logError('Failed to access local camera');
          logError(err.message);
        }
      );
    }
  };

  //REMOTE video
  var showRemoteStream = function (stream) {
    remoteVideo.src = window.URL.createObjectURL(stream);
  };

  //Connect to the PeerJS server
  var connect = function () {
    cId = clientID.value;

    if (!cId) {
      logError('please set client ID first');
      return;
    }

    try {
      // create connection to the ID server
      peer = new Peer(cId, {host: SERVER_IP, port: SERVER_PORT});

	  
      peer.socket._socket.onclose = function () {
        logError('no connection to server');
        peer = null;
      };

	  peer.on('open', function() {
		 logMessage('Connected to server');
	  });
	  
      //Get local stream ready
      peer.socket._socket.onopen = function () {
        getLocalStream();
      };

      // handle events representing incoming calls
      peer.on('call', answer);
    }
    catch (e) {
      peer = null;
      logError('error while connecting to server');
    }
  };

  //Make a stream request
  var streamRequest = function () {
    if (!peer) {
      logError('No connection to server.');
      return;
    }

    if (!localStream) {
      logError('No local camera.');
      return
    }

    var recipientId = streamID.value;

    if (!recipientId) {
      logError('Could not stream as no recipient ID is set');
      return;
    }

    getLocalStream(function (stream) {
      logMessage('Stream initiated.');

      var call = peer.call(recipientId, stream);

      call.on('stream', showRemoteStream);

      call.on('error', function (e) {
        logError('Error with call');
        logError(e.message);
      });
    });
  };

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

  // answer incoming stream request
  var answer = function (call) {
    if (!peer) {
      logError('No connection for incoming stream.');
      return;
    }

    if (!localStream) {
      logError('could not answer call as there is no localStream ready');
      return;
    }

    logMessage('Viewing video stream');

    call.on('stream', showRemoteStream);

    call.answer(localStream);
  };


  // wire up button events
  connectBtn.addEventListener('click', connect);
  streamBtn.addEventListener('click', streamRequest);
});
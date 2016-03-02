var ip = require('ip');
var PeerServer = require('peer').PeerServer;

var port = 9000;
var server = new PeerServer({port: port});

server.on('connection', function (id) {
  console.log('New connection from id ' + id);
});

server.on('disconnect', function (id) {
  console.log('Disconnected from id ' + id);
});

console.log('Peer server is running on ' +
            ip.address() + ':' + port);
var ip = require('ip');
var fs = require('fs');
var PeerServer = require('peer').PeerServer;

var port = 9000;
var server = new PeerServer({port: port, ssl: {
    key: fs.readFileSync('server.key'),
    cert: fs.readFileSync('server.crt')}});

server.on('connection', function (id) {
  console.log('New connection from id ' + id);
});

server.on('disconnect', function (id) {
  console.log('Disconnected from id ' + id);
});

console.log('Peer server is running on ' +
            ip.address() + ':' + port);
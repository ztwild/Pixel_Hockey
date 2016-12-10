//var net = require('net');
//var util = require('util');
//var path = require('path');
//var http = require('http');
//var fs   = require('fs');
////var HOST = '192.168.1.107';///Set host ip to computers ip addrress
//var HOST = '192.168.1.107';
////var HOST = 'localhost';
//var PORT = 8000;
//var playerWaiting;
//
//var server = http.createServer();
//net.createServer(function(sock){ 
//  console.log('CONNECTED: ' + sock.remoteAddress +':'+ sock.remotePort);
//    //Read socket info
////  setTimeout(function(){ }, 5000);
////  sock.emit('message', {message:"see this shit"});
//  //Player joining game
//  sock.on('joinGame', function(data){
////    console.log(data);
////    sock.name = data.name;
//    console.log("Player attempting to join game");
//    if(playerWaiting == undefined || playerWaiting.destroyed){
//        console.log("Waiting to be paired");
//        playerWaiting = sock;
//      }else if(playerWaiting != sock){
//        console.log("paired with partner");
//        sock.partner = playerWaiting;
//        playerWaiting.partner = sock;
//        
//        sock.emit('playerJoin', {name : playerWaiting.name});
//        playerWaiting.emit('playerJoin', {name : sock.name});
//        playerWaiting = undefined;
//      }
//  });  
//  
//  sock.on('sendMove', function(data){
//    sock.partner.emit('getMove', data);
//  });
//  
//  //On Close
//  sock.on('close', function(data){
//    if(sock.partner != undefined){
//      console.log("Writing end game to partner");
//      sock.partner.write("End Game\n");
//      sock.partner.partner = null;
//    }
//    console.log('CLOSED: ' + sock.remoteAddress +' '+ sock.remotePort);
//  });
//    
//  //
//  sock.on('error', function(data){
//    console.log("Player is absent");
//  });
//}).listen(PORT, HOST);
//
//console.log('Server listening on ' + HOST +':'+ PORT);

var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);
var players = [];

server.listen(8000, function(){
	console.log("Server is now running...");
});

io.on('connection', function(socket){
	console.log("Player Connected!");
//	socket.emit('socketID', { id: socket.id });
//	socket.emit('getPlayers', players);
//	socket.broadcast.emit('newPlayer', { id: socket.id });
	socket.on('disconnect', function(){
		console.log("Player Disconnected");
		socket.broadcast.emit('playerDisconnected', { id: socket.id });
		for(var i = 0; i < players.length; i++){
			if(players[i].id == socket.id){
				players.splice(i, 1);
			}
		}
	});
   sock.on("joinGame", function(data){
//    console.log(data);
//    sock.name = data.name;
    console.log("Player attempting to join game");
    if(playerWaiting == undefined || playerWaiting.destroyed){
        console.log("Waiting to be paired");
        playerWaiting = sock;
      }else if(playerWaiting != sock){
        console.log("paired with partner");
        sock.partner = playerWaiting;
        playerWaiting.partner = sock;
        
        sock.emit('playerJoin', {name : playerWaiting.name});
        playerWaiting.emit('playerJoin', {name : sock.name});
        playerWaiting = undefined;
      }
  });  
  
  sock.on('sendMove', function(data){
    sock.partner.emit('getMove', data);
  });
  
	players.push(new player(socket.id, 0, 0));
});

function player(id, x, y){
	this.id = id;
	this.x = x;
	this.y = y;
}
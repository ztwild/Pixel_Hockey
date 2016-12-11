var app = require('express')();
var http = require('http').Server(app);
var io = require('socket.io')(http);

var playerWaiting;

app.get('/', function(req, res){
  res.send('<h1>LibGDX SocketIO client to NodeJS server test, up and running!</h1>');
});

io.on('connection', function(socket)
{
    console.log('a user connected');
    console.log('Socket Object: ' + socket);

    socket.on('joinGame', function(msg)
    {
      
		if(playerWaiting == undefined || playerWaiting.destroyed){
			console.log("Waiting to be paired");
			playerWaiting = socket;
		}else if(playerWaiting != socket){
			console.log("paired with partner");
			io.emit('startgame', msg);
			
			//socket.partner = playerWaiting;
			//playerWaiting.partner = socket;
			
			
			//socket.write("startgame");
			//playerWaiting.write("startgame");
			playerWaiting = undefined;
		}
    });


    socket.on('disconnect', function(msg)
    {
        console.log('Client disconnected with message: ' + msg);
    });

});

http.listen(8000, function(){
  console.log('listening on *:8000');
});



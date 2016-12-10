package com.se339.communication;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.se339.log.Log;
import com.se339.pixel_hockey.PixelHockeyGame;
import com.se339.pixel_hockey.screens.GameScreen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.UnknownHostException;

/**
 * Created by Zach on 12/5/2016.
 */

public class WebSocket {
    private Socket socket = null;
    private Socket sock;
    private PrintWriter out = null;
    private BufferedReader in = null;
    private Log l;

    public WebSocket(){
        l = new Log("WebSocket");
        try {
            socketConnect();
        } catch (UnknownHostException e) {
            l.e("Unknown Host, Cannot Connect");
            e.printStackTrace();
        } catch (IOException e) {
            l.e("Cannot Connect to Server");
            e.printStackTrace();
        }
    }

    //Creates Connection to Web Socket
    public void socketConnect() throws UnknownHostException, IOException {
        String ip = "192.168.1.107";
//        String ip = "localhost";
        int port = 8000;
        System.out.println("[Connecting to socket...]");
        SocketHints hint = new SocketHints();
        hint.connectTimeout = 60;

        sock = Gdx.net.newClientSocket(Net.Protocol.TCP, ip, 8000, hint);//new Socket(ip, port);
//        sock = socket.accept(null);
//        sock = new Socket(ip, port);
        out = new PrintWriter(sock.getOutputStream(), true);
//        in = new BufferedReader(new InputStreamReader(sock.getInputStream()));

    }

    public Socket getSock(){
        return sock;
    }

    public void joinGame(){
        //Print 'Searching for player'
        try{
            sock.getOutputStream().write("joinGame&".getBytes());
        }catch(IOException e ){
            e.printStackTrace();
        }

//        out.println("joinGame&");
    }

//    public String read() throws IOException{
//        return  in.readLine();
//    }

    public void searchResponse(PixelHockeyGame game){
        try{
            String message = in.readLine();
            if(message.equals("startGame")){
                game.setScreen(new GameScreen(game));
            }
        }catch(IOException e){
            l.e("Cannot get Response");
            e.printStackTrace();
        }
    }

    public void endSearch(){
        out.println("endSearch&");
    }

    public void sendMove(Vector2 velocity){
        out.println(velocity+"&");
    }

    public void sendGoal(){
        out.println("Goal&");
    }
}

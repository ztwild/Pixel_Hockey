package com.se339.communication;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.math.Vector2;

import com.se339.log.Log;
import com.se339.pixel_hockey.PixelHockeyGame;
import com.se339.pixel_hockey.screens.GameScreen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.UnknownHostException;

import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * Created by Zach on 12/5/2016.
 */

public class WebSocket {
    private Socket socket = null;
    private PrintWriter out = null;
    private BufferedReader in = null;
    private Log l;

    public WebSocket(){
        l = new Log("WebSocket");
        connectSocket();
    }

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

    public void connectSocket(){
        try{
            //socket = IO.socket("http://localhost:8000");
            socket = IO.socket("http://192.168.1.107:8000");
            socket.connect();
        } catch (Exception e) {
            l.e("Error connecting");
            e.printStackTrace();
        }
    }

    public Socket getSocket(){
        return socket;
    }
}

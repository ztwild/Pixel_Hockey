//package com.se339.communication;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Net;
//import com.badlogic.gdx.math.Vector2;
//
//import com.se339.fileUtilities.UserReader;
//import com.se339.log.Log;
//import com.se339.pixel_hockey.PixelHockeyGame;
//import com.se339.pixel_hockey.screens.GameScreen;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.net.UnknownHostException;
//
//import io.socket.engineio.client.Socket;
//import io.socket.emitter.Emitter;
//
///**
// * Created by Zach on 12/5/2016.
// */
//
//public class WebSocket {
//    private Socket socket = null;
//    private PrintWriter out = null;
//    private BufferedReader in = null;
//    private Log l;
//
//    public WebSocket(){
//        l = new Log("WebSocket");
//        connectSocket();
//    }
//
//    public void searchResponse(PixelHockeyGame game){
//        try{
//            String message = in.readLine();
//            if(message.equals("startGame")){
//                game.setScreen(new GameScreen(game));
//            }
//        }catch(IOException e){
//            l.e("Cannot get Response");
//            e.printStackTrace();
//        }
//    }
//
//    public void endSearch(){
//        out.println("endSearch&");
//    }
//
//    public void sendMove(Vector2 velocity){
//        out.println(velocity+"&");
//    }
//
//    public void sendGoal(){
//        out.println("Goal&");
//    }
//
//    public void connectSocket(){
//
//        try{
//            //socket = IO.socket("http://localhost:8000");
//
//            l.l("Trying to open new socket");
//            socket = new Socket("http://localhost:8000");
//
//            socket.on(Socket.EVENT_OPEN, new Emitter.Listener() {
//                @Override
//                public void call(Object... args) {
//
//                }
//            }).on("playerJoin", new Emitter.Listener() {
//                @Override public void call(Object ... args) {
//                    JSONArray objects = (JSONArray) args[0];
//                    try {
//                        String p2name = (String) objects.getJSONObject(0).getString("name");
//                        l.v(p2name, "Player name");
//                        //setScreen(new GameScreen(PixelHockeyGame.g));
//                    } catch (Exception e) {
//                        l.e("Failed to start new game");
//                    }
//                }
//            }).on("message", new Emitter.Listener() {
//                @Override
//                public void call(Object ... args){
//                    JSONArray objects = (JSONArray) args[0];
//                    try {
//                        String message = (String) objects.getJSONObject(0).getString("message");
//                        l.v(message, "message");
//                        //setScreen(new GameScreen(PixelHockeyGame.g));
//                    } catch (Exception e) {
//                        l.e("Failed to start new game");
//                    }
//                }
//            })
//            ;
//            socket.open();
//
//            l.l("socket connected");
//        } catch (Exception e) {
//            l.e("Disconnected from server");
//            e.printStackTrace();
//        }
//    }
//
//    public Socket getSocket(){
//        return socket;
//    }
//
//    public void serverListener(){
//
//    }
//
//}

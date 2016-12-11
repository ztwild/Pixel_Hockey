package com.se339.pixel_hockey;

/**
 * Created by Zach on 11/30/2016.
 */
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.math.Vector2;

import com.se339.log.Log;
import com.se339.pixel_hockey.screens.GameScreen;
import com.se339.pixel_hockey.screens.MainMenuScreen;
import com.se339.pixel_hockey.sprites.Player;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import io.socket.client.Socket;
import io.socket.client.IO;
import io.socket.emitter.Emitter;

public class PixelHockeyGame extends Game {

    public static PixelHockeyGame g;

    public SpriteBatch batch;
    public BitmapFont font;
    private static int pHeight = 0;
    private static int pWidth = 0;
    public Socket socket;
    public boolean setGameScreen = false;

    public Vector2 puckVelocity;
    public ArrayList<Float> opPosition;
    public boolean opMoved;
    public boolean puckMoved;

    Log log;

    private final int winningscore = 3;

    public void create() {
        log = new Log("PixelHockeyGame");

        batch = new SpriteBatch();
        // Use LibGDX's default Arial font.
        font = new BitmapFont();

        pHeight = Gdx.graphics.getHeight();
        pWidth = Gdx.graphics.getWidth();

        opMoved = false;
        puckMoved = false;

        g = this;
        setScreen(new MainMenuScreen(this));
    }

    public static int getWidth(){
        return pWidth;
    }

    public static int getHeight(){
        return pHeight;
    }

    public void render() {
        super.render(); // important!
    }

    public void dispose() {
        font.dispose();
    }

    public int getWinningScore(){ return winningscore; } 

    public Socket getSocket(){
        return socket;
    }

    public void initsocket(){
        log.l("initializing socket connection");

        try {
            // Michael's
//            socket = IO.socket("http://192.168.1.103:8000");
//            socket = IO.socket("http://10.20.22.133:8000");
            socket = IO.socket("http://10.26.45.170:8000");

            // Zach's
//            socket = IO.socket("http://192.168.1.107:8000");

            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call (Object ... args) {
                    log.l("Connected to server");
                    socket.emit("Connection Ack");
                }
            }).on("startgame", new Emitter.Listener() {

                @Override
                public void call (Object ... arg) {
                    log.l("Starting game");
                    gameScreen();
                }
            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                @Override
                public void call (Object ... args) {
                    log.l("Disconnected from server");
                    socket = null;
                }
            });

            socket.connect();

            log.l("socket connected");
        } catch (Exception e) {
            log.e("could not connect socket");
            e.printStackTrace();
        }
    }

    public void gameScreen(){
        setGameScreen = true;
    }

    public void updateInfo(){
        //log.l("sending game update to server");

        JSONArray o = new JSONArray();
        try {
            JSONObject p = new JSONObject();

            try {
                p.put("x", opPosition.get(0));
                p.put("y", opPosition.get(1));
            } catch (Exception e) {
                log.v(p, "json object");
                log.v(opPosition, "opPosition");
                e.printStackTrace();
            }

            JSONObject v = new JSONObject();

            try {
                v.put("x", puckVelocity.x);
                v.put("y", puckVelocity.y);

            } catch (Exception e) {
                log.v(v, "json object");
                log.v(opPosition, "puckvelocity");
                e.printStackTrace();
            }

            o.put(p);
            o.put(v);

            socket.emit("update", o);
        } catch (Exception e) {
            log.e("Update Info");
            e.printStackTrace();
        }
    }

    public void updateInfo(float x, float y){
        //log.l("sending game update to server");

        JSONArray o = new JSONArray();
        try {
            JSONObject p = new JSONObject();

            try {
                p.put("x", x);
                p.put("y", y);
            } catch (Exception e) {
                log.v(p, "json object");
                log.v(opPosition, "opPosition");
                e.printStackTrace();
            }

            JSONObject v = new JSONObject();

            try {
                v.put("x", puckVelocity.x);
                v.put("y", puckVelocity.y);

            } catch (Exception e) {
                log.v(v, "json object");
                log.v(opPosition, "puckvelocity");
                e.printStackTrace();
            }

            o.put(p);
            o.put(v);

            socket.emit("update", o);
        } catch (Exception e) {
            log.e("Update Info");
            e.printStackTrace();
        }
    }

    public void updateInfo(Vector2 vel){
        //log.l("sending game update to server");

        JSONArray o = new JSONArray();
        try {
            JSONObject p = new JSONObject();

            try {
                p.put("x", opPosition.get(0));
                p.put("y", opPosition.get(1));
            } catch (Exception e) {
                log.v(p, "json object");
                log.v(opPosition, "opPosition");
                e.printStackTrace();
            }

            JSONObject v = new JSONObject();

            try {
                v.put("x", vel.x);
                v.put("y", vel.y);

            } catch (Exception e) {
                log.v(v, "json object");
                log.v(opPosition, "puckvelocity");
                e.printStackTrace();
            }

            o.put(p);
            o.put(v);

            socket.emit("update", o);
        } catch (Exception e) {
            log.e("Update Info");
            e.printStackTrace();
        }
    }


}
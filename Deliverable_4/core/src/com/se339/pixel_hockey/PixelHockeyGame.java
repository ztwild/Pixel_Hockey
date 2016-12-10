package com.se339.pixel_hockey;

/**
 * Created by Zach on 11/30/2016.
 */
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.se339.communication.WebSocket;
import com.se339.log.Log;
import com.se339.pixel_hockey.screens.GameScreen;
import com.se339.pixel_hockey.screens.MainMenuScreen;


import org.json.JSONArray;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class PixelHockeyGame extends Game {

    public static PixelHockeyGame g;

    public SpriteBatch batch;
    public BitmapFont font;
    private static int pHeight = 0;
    private static int pWidth = 0;
    public static WebSocket wb;

    Log log;

    private final int winningscore = 3;

    public void create() {
        log = new Log("PixelHockeyGame");

        batch = new SpriteBatch();
        // Use LibGDX's default Arial font.
        font = new BitmapFont();

        pHeight = Gdx.graphics.getHeight();
        pWidth = Gdx.graphics.getWidth();
        wb = new WebSocket();
        initserver();
        setScreen(new MainMenuScreen(this));
        g = this;
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
        return wb.getSocket();
    }

    public WebSocket getWB(){
        return wb;
    }

    public void initserver(){
        log.l("Createing Emitter Listener");
        wb.getSocket().on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {

            }
        }).on("playerJoin", new Emitter.Listener() {
            @Override public void call(Object ... args) {
                JSONArray objects = (JSONArray) args[0];
                try {
                    String p2name = (String) objects.getJSONObject(0).getString("name");
                    log.v(p2name, "Player name");
                    setScreen(new GameScreen(PixelHockeyGame.g));
                } catch (Exception e) {
                    log.e("Failed to start new game");
                }
            }
        });
    }
}
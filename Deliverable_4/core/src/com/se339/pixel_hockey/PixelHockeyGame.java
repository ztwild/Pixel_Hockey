package com.se339.pixel_hockey;

/**
 * Created by Zach on 11/30/2016.
 */
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.se339.communication.GameValues;
import com.se339.communication.ServerListener;
import com.se339.communication.WebSocket;
import com.se339.pixel_hockey.screens.GameScreen;
import com.se339.pixel_hockey.screens.MainMenuScreen;
import com.se339.pixel_hockey.screens.Screens;

public class PixelHockeyGame extends Game {

    public SpriteBatch batch;
    public BitmapFont font;
    private static int pHeight = 0;
    private static int pWidth = 0;
    public static WebSocket wb;
    private ServerListener sl;
    private final int winningscore = 3;

    public void create() {
        batch = new SpriteBatch();
        // Use LibGDX's default Arial font.
        font = new BitmapFont();

        pHeight = Gdx.graphics.getHeight();
        pWidth = Gdx.graphics.getWidth();
        wb = new WebSocket();
        sl = new ServerListener(this);
        this.setScreen(new MainMenuScreen(this));
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

    public void setSLScreen(GameScreen screen){
        sl.setScreen(screen);
    }
}
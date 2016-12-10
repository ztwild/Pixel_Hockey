package com.se339.pixel_hockey.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.se339.log.Log;
import com.se339.pixel_hockey.PixelHockeyGame;

/**
 * Created by mweem_000 on 12/1/2016.
 */

public class Screens implements Screen {

    public PixelHockeyGame game;
    protected OrthographicCamera camera;

    protected Log log;

    public Screens(PixelHockeyGame game){
        this.game = game;
        log = new Log("Screens");

        // create the camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, PixelHockeyGame.getWidth(), PixelHockeyGame.getHeight());
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {}

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {}
}

package com.se339.pixel_hockey.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.se339.log.Log;
import com.se339.pixel_hockey.PixelHockeyGame;
import com.se339.pixel_hockey.screens.ui_elements.Hud;

/**
 * Created by Michael Weems on 12/5/2016.
 */

public abstract class MenuScreen extends Screens {

    protected Log log;

    protected Stage stage;
    protected Skin skin;

    protected float rC[] = {0f,0f,0f,0f};

    public MenuScreen(PixelHockeyGame game) {
        super(game);

        log = new Log("MenuScreen");

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("data/uiskin.json"));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(rC[0], rC[1], rC[1], rC[1]);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    protected void setRenderColor(float a, float b, float c, float d){
        rC[0] = a;
        rC[1] = b;
        rC[2] = c;
        rC[3] = d;
    }

    /*
     * Create the HUD
     */
    protected void createHUD(){
        log.l("Creating hud");
        Hud hud = new Hud(stage.getWidth(), game, false);
        stage.addActor(hud);
    }

}

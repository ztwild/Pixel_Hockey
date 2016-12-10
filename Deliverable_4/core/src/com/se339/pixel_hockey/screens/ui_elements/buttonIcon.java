package com.se339.pixel_hockey.screens.ui_elements;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.se339.fileUtilities.FileList;
import com.se339.log.Log;
import com.se339.pixel_hockey.PixelHockeyGame;

import java.awt.Button;

/**
 * Created by Zach on 12/1/2016.
 */

public class buttonIcon {

    private Sprite sprite;
    private Log log;

    //    public buttonIcon(Texture texture, float x, float y, float width, float height) {
    public buttonIcon(String img) {
        log = new Log("Buttonicon class");
        sprite = new Sprite(new Texture(img)); // your image
        sprite.setSize(30, 16);
    }

    public ImageButton getBtn() {
        Skin skin = new Skin();
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        log.a("Making pixmap and skins");
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));
        skin.add("default", new BitmapFont());
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = skin.newDrawable("white", Color.LIGHT_GRAY);
        style.down = skin.newDrawable("white", Color.DARK_GRAY);
//        style.imageOver = new TextureRegionDrawable(friendText);
        style.imageUp = new TextureRegionDrawable(sprite);
        log.a("Making Button");
        ImageButton btn = new ImageButton(style);
        return btn;
    }


}
package com.se339.pixel_hockey.screens.ui_elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.se339.fileUtilities.FriendReader;

import java.util.ArrayList;

/**
 * Created by Zach on 12/4/2016.
 */

public class FriendScroll extends Table {

    Skin skin;
    Table container;
//    Stage stage;
    public FriendScroll(float width, float height){
//        stage = new Stage;
        Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));
        container = new Table();

        container.setSize(width, height);
        container.setPosition(0,450);
//
        Table table = new Table();
        final ScrollPane scroll = new ScrollPane(table, skin);
        InputListener stopTouchDown = new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                event.stop();
                return false;
            }
        };
        FriendReader fReader = new FriendReader();
        fReader.init();
        Slider slider = new Slider(0, 100, 1, false, skin);
        slider.addListener(stopTouchDown); // Stops touchDown events from propagating to the FlickScrollPane.
        table.add(slider);
        ArrayList<String> friends = fReader.getFriends();
        Label header = new Label("Name               Wins               Losses", skin);
        header.setFontScale(3,3);
        table.row();
        table.add(header);
        for(String friend : friends){
            table.row();
            Label f = new Label(friend, skin);
            f.setFontScale(3,3);
            table.add(f);
        }
        container.add(scroll).expand().fill().colspan(4);
        container.row().space(10).padBottom(10);
    }
}

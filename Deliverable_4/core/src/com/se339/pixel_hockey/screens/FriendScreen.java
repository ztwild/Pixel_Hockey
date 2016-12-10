package com.se339.pixel_hockey.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.se339.fileUtilities.FriendReader;
import com.se339.log.Log;
import com.se339.pixel_hockey.PixelHockeyGame;

import java.util.ArrayList;

/**
 * Created by Zach on 12/3/2016.
 */

public class FriendScreen extends MenuScreen {

    private Table container;

    public FriendScreen(PixelHockeyGame game) {
        super(game);

        log = new Log("FriendScreen");

        log.l("adding to Tile");
        Table title = new Table();
        if (skin == null)
            log.e("Skin is null");
        Label titleLabel = new Label("Friends List", skin);
        titleLabel.setFontScale(4,4);
        title.add(titleLabel);
        stage.addActor(title);
        title.setSize(260, 195);
        title.setPosition(400, 1700);

        container = new Table();
        container.setSize(stage.getWidth(), 2*stage.getHeight()/3);
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
        scroll.addListener(stopTouchDown);
        ArrayList<String> friends = fReader.getFriends();

        // Create Label for new row
        Label header = new Label("Name               Wins               Losses", skin);
        header.setFontScale(3,3);
        table.row();
        table.add(header);

        // put each friend on a new row
        for(String friend : friends){
            table.row();
            Label f = new Label(friend, skin);
            f.setFontScale(3,3);
            table.add(f);
        }
        container.add(scroll).expand().fill().colspan(4);
        container.row().space(10).padBottom(10);
        stage.addActor(container);

        createHUD();
        setRenderColor(0.5f, 0.3f, 0.1f, 1);
    }
}

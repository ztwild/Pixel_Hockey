package com.se339.pixel_hockey.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.se339.fileUtilities.UserReader;
import com.se339.log.Log;
import com.se339.pixel_hockey.PixelHockeyGame;


/**
 * Created by Zach on 12/3/2016.
 */

public class StatScreen extends MenuScreen{

    public StatScreen(PixelHockeyGame game, String outcome) {
        super(game);

        log = new Log("StatScreen");

        log.l("adding to Tile");
        Table title = new Table();
        Label titleLabel = new Label("Stats List", skin);
        titleLabel.setFontScale(4,4);
        title.add(titleLabel);
        stage.addActor(title);
        title.setSize(260, 195);
        title.setPosition(400, 1700);
//        title.debug();

        Table stats = new Table();
        UserReader ur = new UserReader();

        if(outcome != null){
            Label outcoumeLabel = new Label(outcome, skin);
            outcoumeLabel.setFontScale(4,4);
            stats.add(outcoumeLabel);
            stats.row();
        }
        Label winsLabel = new Label("Wins: "+ur.getWins(), skin);
        winsLabel.setFontScale(4,4);
        stats.add(winsLabel);
        stats.row();
        Label lossesLabel = new Label("Losses: "+ur.getLosses(), skin);
        lossesLabel.setFontScale(4,4);
        stats.add(lossesLabel);
        stats.row();
        Label gamesLabel = new Label("Total Games Played: "+ur.getTotalGames(), skin);
        gamesLabel.setFontScale(4,4);
        stats.add(gamesLabel);
        stats.row();

        stats.setPosition(550,1000);
        stage.addActor(stats);

        createHUD();
        setRenderColor(0.1f, 0.5f, 0.2f, 1);

    }
}

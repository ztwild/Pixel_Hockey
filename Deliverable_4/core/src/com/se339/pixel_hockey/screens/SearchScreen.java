package com.se339.pixel_hockey.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.se339.fileUtilities.UserReader;
import com.se339.log.Log;
import com.se339.pixel_hockey.PixelHockeyGame;
import com.se339.pixel_hockey.sprites.Player;
import com.se339.pixel_hockey.world.ContactBits;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by Zach on 12/5/2016.
 */

public class SearchScreen extends MenuScreen {

    public SearchScreen(PixelHockeyGame game) {
        super(game);

        log = new Log("SearchScreen");

        //log.l("adding to Tile");
        Table table = new Table();
        Label titleLabel = new Label("Searching for\nGame", skin);
        titleLabel.setFontScale(6,6);
        table.add(titleLabel);
        stage.addActor(table);
        table.setSize(260, 195);
        table.setPosition(400, 1350);

        createHUD();
        setRenderColor(0.1f, 0.3f, 0.5f, 1);
        joinGame();
    }

    @Override
    public void render(float delta) {
        if (game.setGameScreen) {

            // add an additional socket listener
            game.socket.on("update", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    JSONArray objects = (JSONArray) args[0];
                    try {
                        // player 2 position
                        game.opPosition = new ArrayList<Float>();
                        game.opPosition.add((float) objects.getJSONObject(0).getDouble("x"));
                        game.opPosition.add((float) objects.getJSONObject(0).getDouble("y"));

                        // puck velocity
                        game.puckVelocity = new Vector2((float) objects.getJSONObject(1)
                                .getDouble("x"),
                                (float) objects.getJSONObject(1).getDouble("x"));

                    } catch(JSONException e){}
                }
            });
            game.setScreen(new GameScreen(game));
        }
    }

//
//    public void searchGame(){
//        log.l("Searching for game");
//        game.wb.joinGame();
////        try{
////            game.wb.getSock().readLine();
////            game.setScreen(new GameScreen(game));
////        }catch(IOException e){
////            System.out.println("Could not recieve message from search");
////        }
//
//
//    }

//    @Override
//    public void render(float delta) {
//        super.render(delta);
//    }

    public void joinGame(){
//        UserReader ur = new UserReader();
//        JSONObject j = new JSONObject();
//
//        try {
//            j.put("name", ur.readName());
//        } catch (JSONException e) {
//            log.e("JSONException");
//        }
//
//        log.l("Emit 'joinGame'");
//        game.getSocket().emit("joinGame", j);

        log.l("sending 'joinGame' to server");
        game.socket.emit("joinGame", "Michael");
    }


}

package com.se339.pixel_hockey.screens;

/**
 * Created by Zach on 11/30/2016.
 */

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.se339.fileUtilities.FileList;
import com.se339.fileUtilities.FriendReader;
import com.se339.fileUtilities.UserReader;
import com.se339.log.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.se339.communication.GameValues;
import com.se339.pixel_hockey.PixelHockeyGame;
import com.se339.pixel_hockey.input.InputHandler;
import com.se339.pixel_hockey.sprites.Goal;
import com.se339.pixel_hockey.sprites.Player;
import com.se339.pixel_hockey.sprites.Puck;
import com.se339.pixel_hockey.sprites.Sprites;
import com.se339.pixel_hockey.world.ContactBits;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static java.awt.SystemColor.info;

public class GameScreen extends Screens {

    //Reference to our Game, used to set Screens
    public static boolean alreadyDestroyed = false;
    public GameValues gvalues;

    //basic playscreen variables
    private OrthographicCamera gamecam;
    private Viewport gamePort;

    //Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;
    private float ppm;

    //sprites
    private ArrayList<Sprites> sprites;
    private Player player;
    private Player oppPlayer;

    private Puck puck;
    private Goal usergoal;
    private Goal oppgoal;
    private Socket socket;

    private Music music;

    public float scalex;
    public float scaley;
    public boolean resetpuck = false;

    public boolean endgame = false;
    public int userscore;
    public int opScore;
    public String opName;

//    private Array<Item> items;
//    private LinkedBlockingQueue<ItemDef> itemsToSpawn;


    public GameScreen(PixelHockeyGame game) {
        super(game);

        log = new Log("GameScreen");

        this.game = game;
        ppm = 500;
        socket = game.getSocket();

        //create cam used to follow mario through cam world
        gamecam = new OrthographicCamera();

        //create a FitViewport to maintain virtual aspect ratio despite screen size
        gamePort = new FitViewport(PixelHockeyGame.getWidth() / ppm, PixelHockeyGame.getHeight() / ppm, gamecam);
        //log.g(PixelHockeyGame.getWidth() / ppm, PixelHockeyGame.getHeight() / ppm, "GamePort Width", "GamePort Height", "Setting GamePort Dimensions");

        //initially set our gamcam to be centered correctly at the start of of map
        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
        //log.g(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, "GameCam X", "GameCam Y", "Setting Gamecam Position");

        scalex = gamePort.getWorldWidth();
        scaley = gamePort.getWorldHeight();


        //create our Box2D world, setting no gravity in X, -10 gravity in Y, and allow bodies to sleep
        world = new World(new Vector2(0, 0), false);
        //allows for debug lines of our box2d world.
//        b2dr = new Box2DDebugRenderer();

        // create all sprites
        player = new Player(this, FileList.image_stick_blue, ContactBits.PLAYER1);

        puck = new Puck(this);
        usergoal = new Goal(this, FileList.image_goal_user,
                (PixelHockeyGame.getWidth() / 2) / ppm, (PixelHockeyGame.getHeight() / 8) / ppm);
        oppgoal = new Goal(this, FileList.image_goal_opp,
                (PixelHockeyGame.getWidth() / 2) / ppm, (7 * PixelHockeyGame.getHeight() / 8) / ppm);
        oppPlayer = new Player(this, FileList.image_stick_green, ContactBits.PLAYER2);

        sprites = new ArrayList<Sprites>();
        sprites.add(player);
        sprites.add(puck);
        sprites.add(usergoal);
        sprites.add(oppgoal);
        sprites.add(oppPlayer);

        Gdx.input.setInputProcessor(new InputHandler(this));
        gvalues = new GameValues(game, this);
        listenConfig();

        UserReader u = new UserReader();

        game.socket.emit("name", u.readName());

        log.g(scalex, scaley, "scalex", "scaley", "game width and height");
        log.v(puck.getSize(), "puck size");
        log.g(scalex - puck.getSize(), scaley - puck.getSize(), "x bound", "y bound", "Bounds");

    }

    public Player[] getPlayers(){
        Player[] players = {player, oppPlayer};
        return players;
    }

    public Player getPlayer(){
        return player;
    }



    public Puck getPuck() { return puck; }
    public float[] getPuckPosition() {
        float xy[] = {0,0};
        xy[0] =  puck.body.getPosition().x;
        xy[1] =  puck.body.getPosition().y;
        return xy;
    }

    public void update(float dt){

        //takes 1 step in the physics simulation(60 times per second)
        //world.step(1 / 120f, 6, 2);

        if (endgame){
            int stat = (userscore > opScore ? 1 : 0);
            game.fr.editStat(opName, stat);
            gvalues.setScores(userscore, opScore);
        }


        if (game.puckVelocity == null)
            game.puckVelocity = new Vector2(0f, 0f);

        if (game.puckMoved) {
            puck.setVelocity(game.puckVelocity);
            game.puckMoved = false;
        }

        try {
            if (game.opPosition == null) {
                game.opPosition = new ArrayList<Float>();
                game.opPosition.add(oppPlayer.posX);
                game.opPosition.add(oppPlayer.posY);

                if (game.opMoved) {
                    oppPlayer.setPosition(oppPlayer.posX, oppPlayer.posY);
                    oppPlayer.body.setTransform(oppPlayer.posX, oppPlayer.posY, 0f);
                    game.opMoved = false;
                }
            }
            else {
                if (game.opMoved) {
                    //log.g(game.opPosition.get(0), game.opPosition.get(1), "x", "y", "game.opPosition - update");
                    //oppPlayer.setPosition(game.opPosition.get(0), game.opPosition.get(1));
                    oppPlayer.body.setTransform(game.opPosition.get(0), game.opPosition.get(1), 0f);
                    game.opMoved = false;
                }
            }
        } catch (Exception e) {
            log.v(oppPlayer, "oppPlayer");
            log.v(game.opPosition,"opPosition");
            e.printStackTrace();
        }

        if (resetpuck) {
            resetPuck();
            resetpuck = false;
        }

        world.step(dt, 6, 2);

        for (Sprites s : sprites)
            s.update(dt);

        gamecam.update();
    }


    @Override
    public void render(float delta) {
        //separate our update logic from render
        update(delta);

        //Clear the game screen with Black
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //renderer our Box2DDebugLines
//        b2dr.render(world, gamecam.combined);

        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        for (Sprites s : sprites)
            s.draw(game.batch);

        game.batch.end();

        if(gameOver()){
            game.setScreen(new MainMenuScreen(game));
            dispose();
        }

    }

    public void listenConfig(){
        socket.on("update", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONArray arr = (JSONArray) args[0];

                try{
                    JSONObject info = arr.getJSONObject(0);
                    float x = (float) info.getDouble("x");
                    float y = (float) info.getDouble("y");
                    System.out.println("-_-_-_-_-_-::: "+x+" ::::: "+y+" :::-_-_-_-_-_-");
                    game.opPosition = new ArrayList<Float>();
                    game.opPosition.add(x);
                    game.opPosition.add(y);
                    game.opMoved = true;

                    JSONObject o = arr.getJSONObject(1);
                    x = -1 * (float) o.getDouble("x");
                    y = -1 * (float) o.getDouble("y");
                    game.puckVelocity = new Vector2(x,y);
                    game.puckMoved = true;

//                    oppPlayer.body.setTransform(x,y,0);
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }).on("reset", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                resetpuck = true;
            }
        }).on("name", new Emitter.Listener() {
            @Override
            public void call(Object... args) {

                try {
                    opName = (String) args[0];
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).on("endgame", new Emitter.Listener() {

            @Override public void call(Object ... args) {

                JSONObject o = (JSONObject) args[0];
                UserReader u = new UserReader();
                try {
                    opScore = (int) o.getInt("p1");
                    userscore = (int) o.getInt("p2");
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                endgame = true;


            }
        });
    }

    public boolean gameOver(){
        return false;
    }

    @Override
    public void resize(int width, int height) {
        //updated our game viewport
        gamePort.update(width,height);
    }

    @Override
    public void dispose() {
        super.dispose();
        world.dispose();
//        b2dr.dispose();
    }

    /******************************************************************
     * GAMEVALUES
     */

    // user scored goal
    public void updateScore(){
        gvalues.updateScore(game);
        resetPuck();
        game.socket.emit("reset");
    }

    public void goalScored(){
        gvalues.goalScored();
        resetPuck();
        game.socket.emit("reset");
    }

    public void resetPuck(){
        float x = (PixelHockeyGame.getWidth() / 2) / this.getPPM();
        float y = (PixelHockeyGame.getHeight() / 2) / this.getPPM();
//        puck.setPosition(x,y);
        puck.setVelocity(0,0);
        puck.body.setTransform(x,y,0);

        game.puckMoved = false;
    }

    public void setPuckVelocity(Vector2 v){
        puck.setVelocity(v);
    }

    public void endGame(String outcome){
        JSONObject p = new JSONObject();
        try {
            UserReader u = new UserReader();

            try {
                p.put("p1", gvalues.getUserscore());
                p.put("p2", gvalues.getOpponentscore());
            } catch (Exception e) {
                e.printStackTrace();
            }

            socket.emit("endgame", p);
        } catch (Exception e) {
            log.e("endgame");
            e.printStackTrace();
        }
        game.setScreen(new StatScreen(game, outcome));
    }

    public GameValues getGameValues(){
        return gvalues;
    }

    /*
     *******************************************************************/

    public float getPPM(){
        return ppm;
    }

    public ArrayList<Sprites> getSprites(){
        return sprites;
    }

    public Goal getUserGoal(){
        return usergoal;
    }

    public Goal getOppGoal(){
        return oppgoal;
    }

    public World getWorld() { return world; }

    public void updateInfo(){
        game.puckVelocity = puck.body.getLinearVelocity();
        game.opPosition = new ArrayList<Float>();
        Vector2 pos = player.body.getPosition();
        float x = scalex - pos.x;
        float y = scaley - pos.y;
        game.opPosition.add(x);
        game.opPosition.add(y);
//        game.opPosition.add(oppPlayer.posX);
//        game.opPosition.add(oppPlayer.posY);

        game.updateInfo();
    }

    public void updateInfo(float x, float y){
        game.puckVelocity = puck.body.getLinearVelocity();
        x = scalex - x;
        y = scaley - y;

//        game.opPosition.add(oppPlayer.posX);
//        game.opPosition.add(oppPlayer.posY);

        game.updateInfo(x,y);
    }

    public void updateInfo(Vector2 v){

        game.opPosition = new ArrayList<Float>();
        Vector2 pos = player.body.getPosition();
        float x = scalex - pos.x;
        float y = scaley - pos.y;
        game.opPosition.add(x);
        game.opPosition.add(y);
//        game.opPosition.add(oppPlayer.posX);
//        game.opPosition.add(oppPlayer.posY);

        game.updateInfo(v);
    }

}
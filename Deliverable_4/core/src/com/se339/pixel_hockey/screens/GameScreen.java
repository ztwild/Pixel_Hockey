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

import java.util.ArrayList;

public class GameScreen extends Screens {

    //Reference to our Game, used to set Screens
    private PixelHockeyGame game;
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
    private Puck puck;
    private Goal usergoal;
    private Goal oppgoal;

    private Music music;

//    private Array<Item> items;
//    private LinkedBlockingQueue<ItemDef> itemsToSpawn;


    public GameScreen(PixelHockeyGame game) {
        super(game);

        log = new Log("GameScreen");

        this.game = game;
        ppm = 500;

        //create cam used to follow mario through cam world
        gamecam = new OrthographicCamera();

        //create a FitViewport to maintain virtual aspect ratio despite screen size
        gamePort = new FitViewport(PixelHockeyGame.getWidth() / ppm, PixelHockeyGame.getHeight() / ppm, gamecam);
        //log.g(PixelHockeyGame.getWidth() / ppm, PixelHockeyGame.getHeight() / ppm, "GamePort Width", "GamePort Height", "Setting GamePort Dimensions");

        //initially set our gamcam to be centered correctly at the start of of map
        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
        //log.g(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, "GameCam X", "GameCam Y", "Setting Gamecam Position");

        //create our Box2D world, setting no gravity in X, -10 gravity in Y, and allow bodies to sleep
        world = new World(new Vector2(0, 0), false);
        //allows for debug lines of our box2d world.
        b2dr = new Box2DDebugRenderer();

        // create all sprites
        player = new Player(this, FileList.image_stick_blue, ContactBits.PLAYER1);
        puck = new Puck(this);
        usergoal = new Goal(this, FileList.image_goal_user,
                (PixelHockeyGame.getWidth() / 2) / ppm, (PixelHockeyGame.getHeight() / 8) / ppm);
        oppgoal = new Goal(this, FileList.image_goal_opp,
                (PixelHockeyGame.getWidth() / 2) / ppm, (7 * PixelHockeyGame.getHeight() / 8) / ppm);

        sprites = new ArrayList<Sprites>();
        sprites.add(player);
        sprites.add(puck);
        sprites.add(usergoal);
        sprites.add(oppgoal);

        Gdx.input.setInputProcessor(new InputHandler(this));
        gvalues = new GameValues(game, this);
        game.setSLScreen(this);

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
        b2dr.render(world, gamecam.combined);

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
        b2dr.dispose();
    }

    /******************************************************************
     * GAMEVALUES
     */

    // user scored goal
    public void goalScored(){
        gvalues.updateScore(game);
    }

    public void resetPuck(){
        puck = new Puck(this);
    }

    public void setPuckVelocity(Vector2 v){
        puck.setVelocity(v);
    }

    public void endGame(String outcome){
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

}
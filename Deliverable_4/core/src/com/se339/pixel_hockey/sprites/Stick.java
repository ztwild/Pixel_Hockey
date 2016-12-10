package com.se339.pixel_hockey.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.se339.fileUtilities.FileList;
import com.se339.log.Log;
import com.se339.pixel_hockey.PixelHockeyGame;
import com.se339.pixel_hockey.screens.GameScreen;
import com.se339.pixel_hockey.world.ContactBits;

/**
 * Created by mweem_000 on 12/4/2016.
 */

public abstract class Stick extends Sprites {

    public Stick(GameScreen screen) {
        super(screen);
        setSprite(FileList.image_stick_blue);
        init();
    }

    public Stick(GameScreen screen, String image){
        super(screen, image);
        init();
    }

    private void init(){
        log = new Log("Stick");
        initSprite((PixelHockeyGame.getWidth() / 2) / screen.getPPM(),
                (PixelHockeyGame.getHeight() / 4) / screen.getPPM(),
                100 / screen.getPPM(),
                true);
        defineStick();
    }

    private void defineStick(){
        if (body != null) world.destroyBody(body);

        defineBody();
    }

    public void defineFixture(short player){
        fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(size / 4);
        fdef.friction = 0.1f;
        fdef.restitution = 0f;
        fdef.density = 1f;
        fdef.filter.categoryBits = player;
        fdef.filter.maskBits = ContactBits.PUCK;

        fdef.shape = shape;
        body.createFixture(fdef).setUserData(this);

//        EdgeShape stick = new EdgeShape();
//        stick.set(new Vector2(0, 0), new Vector2(0, 0));
//
//        fdef.filter.categoryBits = player;
//        fdef.shape = stick;
//        fdef.isSensor = true;
//
//        body.createFixture(fdef).setUserData(this);
    }

    public void move(float xy[]){

        //log.l("Inverting Y-coordinate");
        //log.g(xy[0],xy[1],"posX", "posY", "move() Called");

        posX = xy[0];
        posY = xy[1];
        defineStick();

        //log.g(body.getPosition().x, body.getPosition().y, "Stick X", "Stick Y", "New Stick Position");
    }

    public float getSize(){
        return size;
    }
}

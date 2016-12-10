package com.se339.pixel_hockey.sprites;

import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.se339.log.Log;
import com.se339.pixel_hockey.PixelHockeyGame;
import com.se339.pixel_hockey.screens.GameScreen;
import com.se339.pixel_hockey.world.ContactBits;

/**
 * Created by mweem_000 on 12/5/2016.
 */

public class Goal extends Sprites {

    /*
     * Construct a new Goal
     */
    public Goal(GameScreen screen, String image, float x, float y) {
        super(screen, image);
        init(x,y);
    }

    /*
     * Initialize the Goal variables
     */
    private void init(float x, float y){
        log = new Log("Goal");
        initSprite(x, y, 150 / screen.getPPM(), false);
        defineGoal();
    }

    /*
     * Define the goal body
     */
    private void defineGoal(){
        if (body != null) world.destroyBody(body);
        defineBody();
    }

    @Override
    public void defineFixture(){
        fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(size / 4);
        fdef.restitution = 0f;
        fdef.friction = 0.1f;
        fdef.density = 0.5f;
        fdef.filter.categoryBits = ContactBits.GOAL;
        fdef.filter.maskBits = ContactBits.PLAYER1 |
                ContactBits.PLAYER2 |
                ContactBits.PUCK;

        fdef.shape = shape;
        body.createFixture(fdef).setUserData(this);

//        PolygonShape puck = new PolygonShape();
//        Vector2[] v = new Vector2[4];
//        v[0] = new Vector2(-size, -size).scl(size);
//        v[1] = new Vector2(-size, size).scl(size);
//        v[2] = new Vector2(size, size).scl(size);
//        v[3] = new Vector2(size, -size).scl(size);
//        puck.set(v);
//
//        fdef.shape = puck;
//        fdef.restitution = 0f;
//        fdef.friction = 0f;
//        fdef.filter.categoryBits = ContactBits.PUCK;
//        body.createFixture(fdef).setUserData(this);
    }

    /*
     * Return the regiion that the goal occupies
     */
    public float[] getRegion(){
        float reg[] = {0,0,0,0};
        reg[0] = body.getWorldCenter().x - size;
        reg[1] = body.getWorldCenter().x + size;
        reg[2] = body.getWorldCenter().y - size;
        reg[3] = body.getWorldCenter().y + size;

        //log.g(reg[0], reg[1], "min x", "max x", "x region");
        //log.g(reg[2], reg[3], "min y", "max y", "y region");
        return reg;
    }

}

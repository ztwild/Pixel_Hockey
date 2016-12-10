package com.se339.pixel_hockey.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.reflect.Constructor;
import com.se339.fileUtilities.FileList;
import com.se339.log.Log;
import com.se339.pixel_hockey.PixelHockeyGame;
import com.se339.pixel_hockey.screens.GameScreen;
import com.se339.pixel_hockey.world.ContactBits;

/**
 * Created by mweem_000 on 12/4/2016.
 */

public class Puck extends Sprites {

    public Puck(GameScreen screen) {
        super(screen, FileList.image_puck_blue);
        init();
        //setBounds(0,0, radius / screen.getPPM(), radius / screen.getPPM());
        //setTexture(texture);
    }

    private void init(){
        log = new Log("Puck");
        initSprite((PixelHockeyGame.getWidth() / 2) / screen.getPPM(),
                (PixelHockeyGame.getHeight() / 2) / screen.getPPM(),
                100 / screen.getPPM(),
                true);
        definePuck();
    }



    private void definePuck(){
        if (body != null) world.destroyBody(body);

        defineBody();
        defineFixture();
    }

    public void defineFixture(){
        fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(size / 4);
        fdef.restitution = 0f;
        fdef.friction = 0.1f;
        fdef.density = 0.5f;
        fdef.filter.categoryBits = ContactBits.PUCK;
        fdef.filter.maskBits = ContactBits.PLAYER1 |
        ContactBits.PLAYER2;

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
     * Checks the bounds of the screen - pushes the puck
     * back into the screen if it tries to leave
     */
    private void checkBounds(){
        float vel  = 5f;
        float xVel = 0f;
        float yVel = 0f;

        float x = body.getPosition().x;
        float y = body.getPosition().y;

        //log.g(x,y, "x","y", "Puck - check bounds");

        if (x < size || x > PixelHockeyGame.getWidth() / screen.getPPM() - size) {
            log.g(x,y, "x", "y", "Puck checkBounds() - x out of bounds");
            xVel = vel;
            if (x > PixelHockeyGame.getWidth() / screen.getPPM() - size)
                xVel *= -1;
        }
        else
            xVel = body.getLinearVelocity().x;

        if (y < size || y > PixelHockeyGame.getHeight() / screen.getPPM() - size) {
            log.g(x,y, "x", "y", "Puck checkBounds() - y out of bounds");
            yVel = vel;
            if (y > PixelHockeyGame.getHeight() / screen.getPPM() - size)
                yVel *= -1;
        }
        else
            yVel = body.getLinearVelocity().y;

        setVelocity(new Vector2(xVel, yVel));
    }

    public void checkCollision(Player player) {
        float pos[] = {body.getPosition().x, body.getPosition().y};
        float xy[] = {player.body.getPosition().x, player.body.getPosition().y};

        float playersize = screen.getPlayer().getSize();
        float radii = screen.getPlayer().getSize() + screen.getPuck().getSize();

        boolean left = false;
        boolean down = false;

        double x2 = Math.pow(Math.abs(pos[0] - xy[0]), 2);
        double y2 = Math.pow(Math.abs(pos[1] - xy[1]), 2);

        if (Math.sqrt(x2 + y2) < radii) {
            //            log.g(radii, Math.sqrt(x2 + y2), "radii", "distance", "Collision");
            //
            //            log.g(pos[0], xy[0], "Puck x", "Stick x", "");
            //            log.g(pos[1], xy[1], "Puck y", "Stick y", "");

            //log.l("Puck-Stick Collision");
            if (pos[0] >= xy[0]) {
                xy[0] -= playersize / 4;
            } else {
                left = true;
                xy[0] += playersize / 4;
            }

            if (pos[1] >= xy[1]) {
                xy[1] -= playersize / 4;
            } else {
                down = true;
                xy[1] += playersize / 4;
            }


            float xVel = Math.abs(body.getLinearVelocity().x);
            float yVel = Math.abs(body.getLinearVelocity().y);

            if (left)
                xVel *= -1;
            if (down)
                yVel *= -1;

            setVelocity(new Vector2(xVel, yVel));

            log.g(xVel, yVel, "xVel", "yVel", "Applying velocity to puck");
            log.l("Puck Collision");
            //            log.d();
            //            log.d();
            player.sprite.setPosition(xy[0], xy[1]);

        }
    }

    public void hit(Stick stick){
        log.l("hit() Called");
        setVelocity(0, 5f);
        //log.g(body.getPosition().x, body.getPosition().y, "Stick X", "Stick Y", "New Stick Position");
    }

    public void update(float dt){
        super.update(dt);

        if (checkGoal(screen.getOppGoal()))
            screen.goalScored();

        checkBounds();
        checkCollision(screen.getPlayer());
    }

    public float getSize(){
        return size;
    }

    public boolean checkGoal(Goal goal){
        float reg[] = goal.getRegion();
        float x = body.getPosition().x;
        float y = body.getPosition().y;

        // check if player is in goal
        if (x > reg[0] &&
                x < reg[1] &&
                y > reg[2] &&
                y < reg[3]){

            return true;
        }
        return false;
    }
}

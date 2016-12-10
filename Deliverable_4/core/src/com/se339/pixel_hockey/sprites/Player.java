package com.se339.pixel_hockey.sprites;

import com.se339.fileUtilities.FileList;
import com.se339.log.Log;
import com.se339.pixel_hockey.PixelHockeyGame;
import com.se339.pixel_hockey.screens.GameScreen;
import com.se339.pixel_hockey.world.ContactBits;

/**
 * Created by mweem_000 on 12/5/2016.
 */

public class Player extends Stick{

    private short player;
    private boolean canHit;

    public Player(GameScreen screen, short player) {
        super(screen);
        String img = FileList.image_stick_blue;
        if (player == ContactBits.PLAYER2)
            img = FileList.image_stick_red;
        setSprite(img);
        init(player);
    }

    public Player(GameScreen screen, String image, short player) {
        super(screen, image);
        init(player);
    }

    private void init(short player){
        log = new Log("Player");
        if (player == ContactBits.PLAYER2){
            canHit = false;
            setPosition((PixelHockeyGame.getWidth() / 2) / screen.getPPM(),
                    3* (PixelHockeyGame.getHeight() / 4) / screen.getPPM());
        }
        else
            canHit = true;

        defineFixture();
    }

    @Override
    public void defineFixture() {
        super.defineFixture(player);
    }

    /*
     * Commands from Input
     */

    public float[] modifyForBounds(float xy[]){
        float mod[] = xy;

        if (mod[0] < size )
            mod[0] = size;
        else if (mod[0] > PixelHockeyGame.getWidth() / screen.getPPM() - size)
            mod[0] = PixelHockeyGame.getWidth() / screen.getPPM() - size;

        if (mod[1] < size)
            mod[1] = size;
        else if (mod[1] > PixelHockeyGame.getHeight() / screen.getPPM() - size)
            mod[1] = PixelHockeyGame.getHeight() / screen.getPPM() - size;

        return mod;
    }

    public float[] checkForCollision(float xy[]) {
        float pos[] = screen.getPuckPosition();
        float radii = size + screen.getPuck().getSize();
        boolean left = false;
        boolean right = false;
        boolean up = false;
        boolean down = false;

        double x2 = Math.pow(Math.abs(pos[0] - xy[0]),2);
        double y2 = Math.pow(Math.abs(pos[1] - xy[1]),2);

        if (Math.sqrt(x2 + y2) < radii) {
//            log.g(radii, Math.sqrt(x2 + y2), "radii", "distance", "Collision");
//
//            log.g(pos[0], xy[0], "Puck x", "Stick x", "");
//            log.g(pos[1], xy[1], "Puck y", "Stick y", "");

            //log.l("Puck-Stick Collision");
            if (pos[0] >= xy[0]) {
                right = true;
                xy[0] -= screen.getPlayer().getSize() / 4;
            } else {
                left = true;
                xy[0] += screen.getPlayer().getSize() / 4;
            }

            if (pos[1] >= xy[1]) {
                up = true;
                xy[1] -= screen.getPlayer().getSize() / 4;
            } else {
                down = true;
                xy[1] += screen.getPlayer().getSize() / 4;
            }

            float xVel = 0f;
            float yVel = 0f;

            if (left || right)
                xVel = 5f;
            if (left)
                xVel *= -1f;
            if (down || up)
                yVel = 5f;
            if (down)
                yVel *= -1f;

            log.g(xVel, yVel, "xVel", "yVel", "Applying velocity to puck");
            log.l("Puck Collision");
//            log.d();
//            log.d();
            screen.getPuck().setVelocity(xVel, yVel);
        }
        return xy;
    }

    /*
     * Define the acceptable range of input for the player
     */
    public boolean acceptableInput(float xy[]){

        float goalregion[] = screen.getUserGoal().getRegion();
        log.g(xy[0], xy[1], "touch x coord", "touch y coord", "checking acceptable input");

        boolean acceptable = false;

        // check if not on opponents side of the field
        if (xy[1] < PixelHockeyGame.getHeight() / (2 * screen.getPPM()))
            acceptable = true;

        // check if player is in goal
        if (xy[0] > goalregion[0] &&
                xy[0] < goalregion[1] &&
                xy[1] > goalregion[2] &&
                xy[1] < goalregion[3]){

            acceptable = false;
        }

        log.v(acceptable, "Acceptable input");
        return acceptable;
    }

}

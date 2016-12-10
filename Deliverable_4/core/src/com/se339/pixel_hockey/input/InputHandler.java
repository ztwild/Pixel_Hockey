package com.se339.pixel_hockey.input;

import com.badlogic.gdx.InputProcessor;
import com.se339.log.Log;
import com.se339.pixel_hockey.PixelHockeyGame;
import com.se339.pixel_hockey.screens.GameScreen;
import com.se339.pixel_hockey.sprites.Sprites;
import com.badlogic.gdx.Input;

/**
 * Created by mweem_000 on 12/4/2016.
 */

public class InputHandler implements InputProcessor {

    private GameScreen screen;

    private Log log;

    public InputHandler(GameScreen screen){
        this.screen = screen;
        log = new Log("Input");
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {


//        if(keycode == Input.Keys.RIGHT)
//            body.setLinearVelocity(1f, 0f);
//        if(keycode == Input.Keys.LEFT)
//            body.setLinearVelocity(-1f,0f);
//
//        if(keycode == Input.Keys.UP)
//            body.applyForceToCenter(0f,10f,true);
//        if(keycode == Input.Keys.DOWN)
//            body.applyForceToCenter(0f, -10f, true);
//
//        // On brackets ( [ ] ) apply torque, either clock or counterclockwise
//        if(keycode == Input.Keys.RIGHT_BRACKET)
//            torque += 0.1f;
//        if(keycode == Input.Keys.LEFT_BRACKET)
//            torque -= 0.1f;
//
//        // Remove the torque using backslash /
//        if(keycode == Input.Keys.BACKSLASH)
//            torque = 0.0f;

        // If user hits spacebar, reset everything back to normal
        if(keycode == Input.Keys.SPACE) {

            int i = 0;
            float x = (PixelHockeyGame.getWidth() / 2) / screen.getPPM();
            float y = (PixelHockeyGame.getHeight() / 2) / screen.getPPM();
            float z = (PixelHockeyGame.getHeight() / 4) / screen.getPPM();
            for (Sprites s : screen.getSprites()) {
                s.body.setLinearVelocity(0f, 0f);

                if (i == 1) s.getSprite().setPosition(x, y);
                else s.getSprite().setPosition(x,z);

                s.body.setTransform(0f, 0f, 0f);
                i++;
            }
        }

//        if(keycode == Input.Keys.COMMA) {
//            body.getFixtureList().first().setRestitution(body.getFixtureList().first().getRestitution()-0.1f);
//        }
//        if(keycode == Input.Keys.PERIOD) {
//            body.getFixtureList().first().setRestitution(body.getFixtureList().first().getRestitution()+0.1f);
//        }
//        if(keycode == Input.Keys.ESCAPE || keycode == Input.Keys.NUM_1)
//            drawSprite = !drawSprite;

        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }


    // On touch we apply force from the direction of the users touch.
    // This could result in the object "spinning"
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        //log.g(screenX, screenY, "screenX", "screenY", "User Input - Touch Down");
        return doTouchDownAction(screenX, screenY);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        //log.l("User Input - Touch Drag");
        //log.g(screenX, screenY, "screenX", "screenY", "User Input - Touch Dragged");
        return doTouchDownAction(screenX, screenY);
    }

    private boolean doTouchDownAction(int x, int y){

        y = PixelHockeyGame.getHeight() - y;

        float xy[] = convertToMeters(x,y);

        if (!screen.getPlayer().acceptableInput(xy)) {
            //log.l("Input is not within player bounds");
            return false;
        }

        xy = screen.getPlayer().checkForCollision(xy);
        xy = screen.getPlayer().modifyForBounds(xy);

        //log.g(xy[0], xy[1], "modified x", "modified y", "User Input (now within bounds)");
        //log.g(xy[0], xy[1], "x", "y", "Inverted Y");

        screen.getPlayer().move(xy);
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    private float[] convertToMeters(int x, int y){
        float m[] = {x / screen.getPPM(), y / screen.getPPM()};
        return m;
    }



}

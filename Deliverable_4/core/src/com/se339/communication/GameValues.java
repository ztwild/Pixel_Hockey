package com.se339.communication;

import com.badlogic.gdx.math.Vector2;
import com.se339.fileUtilities.FriendReader;
import com.se339.fileUtilities.UserReader;
import com.se339.pixel_hockey.PixelHockeyGame;
import com.se339.pixel_hockey.screens.GameScreen;
import com.se339.pixel_hockey.world.ContactBits;

/**
 *
 * Class to forward information to the websocket
 *
 * Created by Michael Weems on 12/5/2016.
 */
public class GameValues {

    // player information
    private int userscore;
    private int opponentscore;

    // puck information
    private Vector2 puckVelocity;
    private PixelHockeyGame game;
    private GameScreen screen;
    private FriendReader fr;

    /*
     * Construct a Gamevalues Object
     */
    public GameValues(PixelHockeyGame game, GameScreen screen){
        this.game = game;
        this.screen = screen;
        userscore = 0;
        opponentscore = 0;

        puckVelocity = new Vector2(0f, 0f);
        fr = new FriendReader();
    }

    public void reset(){
        userscore = 0;
        opponentscore = 0;
        puckVelocity = new Vector2(0f,0f);
    }
    /*
     * Update the velocity of the puck - user
     */
    public void updateVelocity(Vector2 v){
        puckVelocity = v;
    }

    /*
     * Update from the sserver
     */
    public void setVelocity(Vector2 v){
        puckVelocity = v;
        screen.setPuckVelocity(puckVelocity);
    }

    /*
     * Increment the score of a player
     */
    public void updateScore(PixelHockeyGame game){
        userscore++;
        //game.sendGoal();
        checkMaxPoints();
    }

    // opponent scored
    public void goalScored(){
        opponentscore++;
        checkMaxPoints();
    }

    /*
     * Get the puck velocity
     */
    public Vector2 getPuckVelocity(){
        return puckVelocity;
    }

    /*
     * Get the current score
     */
    public int[] getScore(){
        int score[] = {userscore, opponentscore};
        return score;
    }

    public void checkMaxPoints(){
        if (userscore >= game.getWinningScore()){
            setPoints();
            screen.endGame("winner");
        }
        else if (opponentscore >= game.getWinningScore()){
            setPoints();
            screen.endGame("loser");
        }
    }

    public void setPoints(){
        UserReader ur = new UserReader();
        ur.setGameScores(userscore, opponentscore);
    }

    public int getUserscore(){
        return userscore;
    }
    public int getOpponentscore(){
        return opponentscore;
    }

    public void setScores(int u, int o){
        userscore = u;
        opponentscore = o;
        checkMaxPoints();
    }
}

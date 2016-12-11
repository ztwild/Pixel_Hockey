package com.se339.fileUtilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.se339.log.Log;

/**
 * Created by Zach on 12/4/2016.
 */

public class UserReader {
    Log log = new Log("Starting txt reader");

    public void writeName(String name){
        Preferences pref = Gdx.app.getPreferences("User Info");
        pref.putString("name", name);
        pref.flush();
    }

    public String readName(){
        Preferences pref = Gdx.app.getPreferences("User Info");
        String name = pref.getString("name");
        return name;
    }

    public int getWins(){
        Preferences pref = Gdx.app.getPreferences("User Info");
        int wins = pref.getInteger("wins");
        return wins;
    }

    public void writeWin(){
        Preferences pref = Gdx.app.getPreferences("User Info");
        pref.putInteger("wins", getWins() + 1);
        pref.flush();
    }

    public int getLosses(){
        Preferences pref = Gdx.app.getPreferences("User Info");
        int losses = pref.getInteger("losses");
        return losses;
    }

    public void writeLoss(){
        Preferences pref = Gdx.app.getPreferences("User Info");
        pref.putInteger("losses", getLosses() + 1);
        pref.flush();
    }

    //Can be uncompleted games such as a timed out game or disconnect
    public int getTotalGames(){
        Preferences pref = Gdx.app.getPreferences("User Info");
        int games = pref.getInteger("games");
        return games;
    }

    public void setGameScores(int myScore, int oppScore){
        Preferences pref = Gdx.app.getPreferences("User Info");
        pref.putInteger("myScore", myScore);
        pref.putInteger("oppScore", oppScore);
        if(myScore > oppScore){
            writeWin();
        }else if(myScore < oppScore){
            writeLoss();
        }
    }

    public int getMyScore(){
        Preferences pref = Gdx.app.getPreferences("User Info");
        return pref.getInteger("myScore");
    }

    public int getOppScore(){
        Preferences pref = Gdx.app.getPreferences("User Info");
        return pref.getInteger("oppScore");
    }
}

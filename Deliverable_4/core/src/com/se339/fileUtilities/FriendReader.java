package com.se339.fileUtilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import java.util.ArrayList;

/**
 * Created by Zach on 12/4/2016.
 */

public class FriendReader {

    private String fileName;
    private ArrayList<String[]> friends;
    private static String line = "";
    private static String split = ",";

    public void init(){
        Preferences pref = Gdx.app.getPreferences("Friend Info");
        for(int i = 0; i < 26; i++){
            pref.putString("player"+i, "ztwild");
            pref.putInteger("win"+i, 4);
            pref.putInteger("lose"+i, 3);
        }
        pref.flush();
    }

    public ArrayList<String> getFriends(){
        ArrayList<String> arr = new ArrayList<String>();
        Preferences pref = Gdx.app.getPreferences("Friend Info");
        boolean loop = true;
        for(int i = 0; i < 30; i++){
            String temp = pref.getString("player"+i);
            if(temp.length() > 0){
                int win = pref.getInteger("win"+i);
                int lose = pref.getInteger("lose"+i);
                temp = temp+"                      "+win+"                          "+lose;
                arr.add(temp);
            }
        }
        return arr;
    }

    //Edit the player stats for wins and losses
    public void editStat(String name, int winPoint){
        int losePoint = 1- winPoint;
        boolean newPlayer = true;
        Preferences pref = Gdx.app.getPreferences("Friend Info");
        for(int i = 0; i < 30; i++){
            String temp = pref.getString("player"+i);
            if(temp.equals(temp)){
                int wintemp = pref.getInteger("win"+i);
                pref.putInteger("win"+i, wintemp+winPoint);
                int losetemp = pref.getInteger("lose"+i);
                pref.putInteger("lose"+i, losetemp+losePoint);
                newPlayer = false;
                break;
            }
        }if(newPlayer){
            shift();
            pref.putString("player29", name);
            pref.putInteger("win29", winPoint);
            pref.putInteger("lose29", losePoint);
        }
        pref.flush();
    }


    public void shift(){
        Preferences pref = Gdx.app.getPreferences("Friend Info");
        for(int i = 0; i < 29; i++){
            String name = pref.getString("player"+(i+1));
            pref.putString("player"+i, name);
            int win = pref.getInteger("win"+(i+1));
            pref.putInteger("win"+i, win);
            int lose = pref.getInteger("lose"+(i+1));
            pref.putInteger("lose"+i, win);

        }
    }
}

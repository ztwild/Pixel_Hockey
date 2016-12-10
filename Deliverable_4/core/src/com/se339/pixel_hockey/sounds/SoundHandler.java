package com.se339.pixel_hockey.sounds;

import com.badlogic.gdx.audio.Music;
import com.se339.fileUtilities.Directory;
import com.se339.fileUtilities.DirectoryList;

import java.util.ArrayList;


/**
 * Created by mweem_000 on 12/1/2016.
 */

public class SoundHandler {

    private ArrayList<String> musiclist;
    private int music_index = 0;
    private Music music;

    public SoundHandler(){
        //music = new Music();
        musiclist = Directory.getFileNames(DirectoryList.dMusic);
    }


}

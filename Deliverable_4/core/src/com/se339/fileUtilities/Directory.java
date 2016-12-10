package com.se339.fileUtilities;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by mweem_000 on 12/1/2016.
 */

public abstract class Directory {

    public static ArrayList<String> getFileNames(String directoryPath){
        File folder = new File(directoryPath);
        File[] listOfFiles = folder.listFiles();
        ArrayList<String> names = new ArrayList<String>();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                names.add(listOfFiles[i].getName());
            }
        }

        return names;
    }

    public static ArrayList<String> getFileNamesFullPath(String directoryPath){
        File folder = new File(directoryPath);
        File[] listOfFiles = folder.listFiles();
        ArrayList<String> names = new ArrayList<String>();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                names.add(listOfFiles[i].getPath());
            }
        }

        return names;
    }

    public static ArrayList<String> getSubDirectoryNames(String directoryPath){
        File folder = new File(directoryPath);
        File[] listOfFiles = folder.listFiles();
        ArrayList<String> names = new ArrayList<String>();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isDirectory()) {
                names.add(listOfFiles[i].getName());
            }
        }

        return names;
    }


}

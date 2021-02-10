package model;

public class Music {

    //-----------Attributes
    private String songName;
    private String path;

    //-----------Constructor
    public Music(String songName, String path) {

        this.songName = songName;
        this.path = path;
    }

    //-----------Getters & Setters

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
    //----------
}
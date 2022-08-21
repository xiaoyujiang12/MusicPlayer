package com.example.musicplayer;

public class PlayList {
    private String songname;
    private String maker;
    public String getSongName() {
        return songname;
    }
    public String getMaker() {
        return maker;
    }
    public PlayList(String songname, String maker) {
        this.songname = songname;
        this.maker = maker;
    }
}



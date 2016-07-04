package dev.datvt.musicequalizer.list_song;

import android.graphics.Bitmap;

/**
 * Created by datvt on 6/30/2016.
 */
public class Music {
    private long id;
    private String name;
    private String author;
    private String time;
    private Bitmap albumArt;
    private String pathSong;


    public Music() {

    }

    public Music(String name, String author, String time, Bitmap albumArt) {
        this.name = name;
        this.author = author;
        this.time = time;
        this.albumArt = albumArt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Bitmap getAlbumArt() {
        return albumArt;
    }

    public void setAlbumArt(Bitmap albumArt) {
        this.albumArt = albumArt;
    }

    public String getPathSong() {
        return pathSong;
    }

    public void setPathSong(String pathSong) {
        this.pathSong = pathSong;
    }

    @Override
    public String toString() {
        return name + " - " + author + " - " + time;
    }
}

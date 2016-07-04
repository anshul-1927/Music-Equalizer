package dev.datvt.musicequalizer;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;

import dev.datvt.musicequalizer.list_song.Music;

/**
 * Created by datvt on 7/1/2016.
 */
public class MusicService extends Service implements MediaPlayer.OnCompletionListener {

    private final IBinder musicBind = new MusicBinder();
    private MediaPlayer mediaPlayer = null;
    private ArrayList<Music> songs;
    private int songPosition;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        songPosition = 0;
        mediaPlayer = new MediaPlayer();
        initMusicPlayer();
    }

    public void initMusicPlayer() {
        mediaPlayer.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnCompletionListener(this);
    }

    public void setList(ArrayList<Music> musicArrayList) {
        songs = musicArrayList;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
        return false;
    }

    public void playSong() {
        mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(songs.get(songPosition).getPathSong()));
        mediaPlayer.start();
    }

    public void stopSong() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void pauseSong() {

    }

    public void loopSong(int loop, int pos) {
        if (mediaPlayer != null) {
            switch (loop) {
                case 0:
                    mediaPlayer.setLooping(false);
                    break;
                case 1:
                    mediaPlayer.setLooping(true);
                    songPosition = pos;
                    break;
            }

        }
    }

    public void setSong(int songIndex) {
        songPosition = songIndex;
    }


    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        Log.d("COMPLETE", "MUSICC");
        if (mediaPlayer != null) {
            nextSong();
        }
    }

    public void nextSong() {
        songPosition++;
        if (++songPosition >= songs.size()) {
            songPosition = 0;
        }
        playSong();
    }

    public class MusicBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

}
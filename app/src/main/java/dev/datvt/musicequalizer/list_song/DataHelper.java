package dev.datvt.musicequalizer.list_song;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import dev.datvt.musicequalizer.R;

/**
 * Created by datvt on 7/1/2016.
 */
public class DataHelper {

    public ArrayList<Music> getDataMusic(Context context) {

        ContentResolver musicResolver = context.getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        Cursor mCursor = musicResolver.query(musicUri, null, selection, null, sortOrder);

        int count = mCursor.getCount();
        Log.d("COUNT_SONGS = ", count + "");

        ArrayList<Music> songs = new ArrayList<>();
        if (mCursor.moveToFirst()) {
            do {
                Music music = new Music();

                long id = mCursor.getLong(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                String title = mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                long duration = mCursor.getLong(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                String artist = mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                String data = mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                long album_id = mCursor.getLong(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));

                Uri sArtworkUri = Uri
                        .parse("content://media/external/audio/albumart");
                Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, album_id);
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(
                            context.getContentResolver(), albumArtUri);
                    bitmap = Bitmap.createScaledBitmap(bitmap, 30, 30, true);

                } catch (FileNotFoundException exception) {
                    exception.printStackTrace();
                    bitmap = BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.ic_album);
                } catch (IOException e) {

                    e.printStackTrace();
                }
                String song_duration = "";
                long minus = TimeUnit.MILLISECONDS.toMinutes(duration);
                long second = TimeUnit.MILLISECONDS.toSeconds(duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration));
                if (minus < 10) {
                    if (second < 10) {
                        song_duration = "0" + minus + ":" + "0" + second;
                    } else {
                        song_duration = "0" + minus + ":" + second;
                    }
                } else {
                    if (second < 10) {
                        song_duration = minus + ":" + "0" + second;
                    } else {
                        song_duration = minus + ":" + second;
                    }
                }

                music.setId(id);
                music.setName(title);
                music.setTime(song_duration);
                music.setAuthor(artist);
                music.setAlbumArt(bitmap);
                music.setPathSong(data);

//                Log.d("MUSIC", name + " - " + id + " - " + title + " - " + duration + " - " + album + " - " + artist + " - " +
//                        data + " - " + size + " - ");
                songs.add(music);
            } while (mCursor.moveToNext());
        }

        mCursor.close();
        return songs;
    }
}

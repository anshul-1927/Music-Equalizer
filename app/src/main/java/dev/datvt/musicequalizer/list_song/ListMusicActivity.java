package dev.datvt.musicequalizer.list_song;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import dev.datvt.musicequalizer.R;

/**
 * Created by datvt on 6/30/2016.
 */
public class ListMusicActivity extends Activity {

    ListView listMusic;
    ListMusicAdapter listMusicAdapter;
    ArrayList<Music> musicArrayList;
    DataHelper dataHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_music);

        listMusic = (ListView) findViewById(R.id.listMusic);
        musicArrayList = new ArrayList<>();
        dataHelper = new DataHelper();
        musicArrayList = dataHelper.getDataMusic(this);

        listMusicAdapter = new ListMusicAdapter(this, musicArrayList);
        listMusic.setAdapter(listMusicAdapter);

        listMusic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Music music = (Music) listMusicAdapter.getItem(i);
                Log.d("TEST", music.toString());
                Intent intent = getIntent();
                intent.putExtra("position", i);
                setResult(101, intent);
                finish();
            }
        });
    }

}

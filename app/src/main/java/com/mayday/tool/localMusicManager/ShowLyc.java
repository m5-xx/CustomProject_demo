package com.mayday.tool.localMusicManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.mayday.xy.customproject.R;

/**
 * Created by xy-pc on 2017/6/25.
 */

public class ShowLyc extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.lyc_show);

        Intent intent = getIntent();
        final String musicTitle = intent.getStringExtra("musicTitle");
        final String musicSinger = intent.getStringExtra("musicSinger");

        Log.i("ShowLyc", "歌名---------->> "+musicTitle);
        Log.i("ShowLyc", "歌手---------->> "+musicSinger);


    }
}

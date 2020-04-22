package com.example.music;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class Player extends AppCompatActivity {
    Button pause,next,previous;
    TextView tvSongName;
    SeekBar seekBar;
    static MediaPlayer mMediaPlayer;
    int position;
    String sname;
    ArrayList<File> mySongs;
    Thread updateseekBar;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

       tvSongName = findViewById(R.id.tvSongName);
       seekBar = findViewById(R.id.seekBar);

       getSupportActionBar().setTitle("Now Playing");
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       getSupportActionBar().setDisplayShowHomeEnabled(true);


       updateseekBar = new Thread(){
           @Override
           public void run() {
               int totalDuration = mMediaPlayer.getDuration();
               int currentPosition = 0;
               while (currentPosition < totalDuration) {
                   try {
                       sleep(500);
                       currentPosition = mMediaPlayer.getCurrentPosition();
                       seekBar.setProgress(currentPosition);

                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               }
           }
       };

       if(mMediaPlayer != null){
           mMediaPlayer.stop();
           mMediaPlayer.release();
       }

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        mySongs=(ArrayList) bundle.getParcelableArrayList("songs");

        sname = mySongs.get(position).toString();

        String songName = intent.getStringExtra("songname");

        tvSongName.setText(songName);
        tvSongName.setSelected(true);

            position = bundle.getInt("pos",0);
            Uri uri = Uri.parse(mySongs.get(position).toString());
            mMediaPlayer.start();
            seekBar.setMax(mMediaPlayer.getDuration());

            updateseekBar.start();

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                    mMediaPlayer.seekTo(seekBar.getProgress());
                }
            });




    }

    public void onPause(View view) {

        seekBar.setMax(mMediaPlayer.getDuration());
        if(mMediaPlayer.isPlaying())
        {
            pause.setBackgroundResource(R.drawable.play);
            mMediaPlayer.pause();
        }
        else{
            pause.setBackgroundResource(R.drawable.pause);
            mMediaPlayer.start();
        }

    }

    public void onNext(View view) {
        mMediaPlayer.stop();
        mMediaPlayer.release();
        position = ((position+1)% mySongs.size());

        Uri uri = Uri.parse(mySongs.get(position).toString());
        mMediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
        sname = mySongs.get(position).getName().toString();
        tvSongName.setText(sname);
        mMediaPlayer.start();
    }

    public void onPrevious(View view) {

        mMediaPlayer.stop();
        mMediaPlayer.release();
        position = ((position-1)<0)? (mySongs.size()-1):(position-1);

        Uri uri = Uri.parse(mySongs.get(position).toString());
        mMediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
        sname = mySongs.get(position).getName().toString();
        tvSongName.setText(sname);
        mMediaPlayer.start();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}

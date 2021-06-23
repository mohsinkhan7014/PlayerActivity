package com.example.playeractivity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.net.URI;
import java.sql.Time;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    CircleImageView imageView;
    TextView startSong,endSong;
    ImageButton btnNext,btnPre,btnPause;
    TextView songTextLabel;
    SeekBar songSeekBar;

    public static String sName;
    static MediaPlayer myMediaPlayer;
    int position=0;
    public static ArrayList<File> mysong;
    Thread updateSeekbar;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnNext=findViewById(R.id.next);
        btnPause=findViewById(R.id.pasue);
        btnPre=findViewById(R.id.preivous);
        songTextLabel=findViewById(R.id.text);
        songSeekBar=findViewById(R.id.seekbar);

        startSong=findViewById(R.id.staringTime);
        endSong=findViewById(R.id.endTime);

        //animation
        imageView=findViewById(R.id.image);
        Animation animation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotation);

        getSupportActionBar().setTitle("Now Playing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        updateSeekbar=new Thread() {
            @Override
            public void run() {
              int totalDuration=myMediaPlayer.getDuration();
              int currentPosition=0;

              while(currentPosition<totalDuration)
              {

                   try {
                        sleep(500);
                        currentPosition=myMediaPlayer.getCurrentPosition();
                        songSeekBar.setProgress(currentPosition);

                   }catch (InterruptedException e)
                   {
                       e.printStackTrace();
                   }
              }

            }
        };


        if(myMediaPlayer!=null)
        {
            myMediaPlayer.stop();
            myMediaPlayer.release();
        }
        Intent i=getIntent();
        Bundle bundle=i.getExtras();
        mysong=(ArrayList) bundle.getParcelableArrayList("song");
        sName=mysong.get(position).getName();

        //start thread

//         songSeekBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.black),PorterDuff.Mode.DARKEN);
//         songSeekBar.getThumb().setColorFilter(getResources().getColor(R.color.design_default_color_on_secondary));

        String songName=i.getStringExtra("SongTittle");
        songTextLabel.setText(songName);
        songTextLabel.setSelected(true);


        position=bundle.getInt("pos",0);

        Uri myUri= Uri.parse(mysong.get(position).toString());

        myMediaPlayer=MediaPlayer.create(getApplicationContext(),myUri);
        myMediaPlayer.start();




        songSeekBar.setMax(myMediaPlayer.getDuration());
        updateSeekbar.start();
        animation.setDuration(myMediaPlayer.getDuration());

        imageView.startAnimation(animation);

        //song total time
        endSong.setText(((myMediaPlayer.getDuration()/1000)/60)+":"+((myMediaPlayer.getDuration()/1000)%60));



        //song seek bar
        songSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //startSong.setText(progress/1000+"");
                int sec=seekBar.getProgress()/1000;
                //int min=sec/60;
                startSong.setText(sec/60+":"+sec%60+"");



            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                     myMediaPlayer.seekTo(seekBar.getProgress());

            }
        });


        btnPause.setOnClickListener(x->{
            songSeekBar.setMax(myMediaPlayer.getDuration());
            if(myMediaPlayer.isPlaying())
            {
                btnPause.setBackgroundResource(R.drawable.icon_play);
                myMediaPlayer.pause();
            }
            else
            {

                btnPause.setBackgroundResource(R.drawable.icon_pause);
                myMediaPlayer.start();
            }
        });

        btnNext.setOnClickListener(x->{
            myMediaPlayer.stop();
            myMediaPlayer.release();
            position+=1%mysong.size();
            if(position!=mysong.size()) {

                Uri u = Uri.parse(mysong.get(position).toString());
                myMediaPlayer = MediaPlayer.create(getApplicationContext(), u);
                sName = mysong.get(position).getName();
                songTextLabel.setText(sName);
                myMediaPlayer.start();
                Toast.makeText(this, "Next Song", Toast.LENGTH_SHORT).show();
            }
            else
            {

                Toast.makeText(this, "No More Next Song", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),songList.class));
            }
        });

        btnPre.setOnClickListener(x->{
            myMediaPlayer.stop();
            myMediaPlayer.release();
            if(position>0) {
                position = ((position - 1) < 0) ? (mysong.size() - 1) : (position - 1);

                Uri u = Uri.parse(mysong.get(position).toString());
                myMediaPlayer = MediaPlayer.create(getApplicationContext(), u);
                sName = mysong.get(position).getName();
                songTextLabel.setText(sName);
                myMediaPlayer.start();

                Toast.makeText(this, "Previous Song", Toast.LENGTH_SHORT).show();
            }
            else
            {
                startActivity(new Intent(getApplicationContext(),songList.class));
                Toast.makeText(this, "See The List", Toast.LENGTH_SHORT).show();
            }

        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
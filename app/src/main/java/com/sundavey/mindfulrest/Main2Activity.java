package com.sundavey.mindfulrest;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import static com.sundavey.mindfulrest.R.id.sound;
import static com.sundavey.mindfulrest.R.mipmap.stop;
import static java.lang.Long.parseLong;

public class Main2Activity extends AppCompatActivity implements OnChronometerTickListener{

    private ImageButton playBtn, selectSoundBtn;
    private TextView mSoundView, mPlaytv, mMintv;
    private MediaPlayer mMediaPlayer;
    private long mediDuration = 0;
    private int soundPosition = -1;
    private ArrayList<Sound> sounds;
    private ProgressBar mProgressBar;
    private Boolean isPlaying = false;
    private Chronometer mChronometer;
    private ObjectAnimator animation;
    private long ChronometerTime = mediDuration * 60000;

    protected MediaPlayer.OnCompletionListener mCompleteListner = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseMediaPlayer();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        sounds = new ArrayList<Sound>();

        sounds.add(new Sound("Singing Bowl", "12sec", R.raw.tibetan_singing_bowl));
        sounds.add(new Sound("Peaceful Woods", "11mins 25sec", R.raw.woods_walk));
        sounds.add(new Sound("Cave", "1min 15sec", R.raw.cave ));
        sounds.add(new Sound("Waves","1min 16sec", R.raw.waves ));
        sounds.add(new Sound("Wind", "48sec", R.raw.waves));

        playBtn = (ImageButton) findViewById(R.id.playbtn);
        selectSoundBtn = (ImageButton) findViewById(R.id.selectSound);

        mMintv = (TextView) findViewById(R.id.minuteInput);
        mSoundView = (TextView) findViewById(sound);
        mProgressBar = (ProgressBar) findViewById(R.id.circularProgressBar);
        mPlaytv = (TextView) findViewById(R.id.playtv);
        mChronometer = (Chronometer) findViewById(R.id.chronometer);

        selectSoundBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main2Activity.this, SelectSound.class);
                startActivityForResult(intent, 1);
            }
        });

        mChronometer.setOnChronometerTickListener(new OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                String meditationTime = mMintv.getText().toString();
                String ChronometerTime = "\"0" + meditationTime + ":00\"";
                Log.d("Main2Activity", meditationTime);
                Log.d("Main2Activity", ChronometerTime);
                if(chronometer.getText().toString().equalsIgnoreCase(ChronometerTime)){
                    mChronometer.stop();
                    mChronometer.setFormat(null);
                    animation.cancel();
                }
            }
        });
    }

    protected void StartBtnClicked(View v){

        Sound sound;

        if(soundPosition == -1){
            sound = sounds.get(0);
        }else{
            sound = sounds.get(soundPosition);
        }
        //true for PLAYing status false for paused status
        Boolean soundPlaying = checkSoundStatus();
        //when ready to play status
        if(!soundPlaying) {
            mMediaPlayer = MediaPlayer.create(Main2Activity.this, sound.getSoundResourceId());
            mMediaPlayer.start();
            mMediaPlayer.setOnCompletionListener(mCompleteListner);
            mChronometer.setBase(SystemClock.elapsedRealtime());
            mChronometer.start();
            playBtn.setBackgroundResource(stop);
            mPlaytv.setText("");
            mPlaytv.setText("STOP");
            animation = ObjectAnimator.ofInt(mProgressBar, "progress", 0, 6*60000);
            animation.setDuration(6000);
            animation.setInterpolator(new DecelerateInterpolator());
            animation.start();
        }else{
            //user clicked on pause button so let the button back to "PLAY"
            playBtn.setBackgroundResource(R.mipmap.play);
            mPlaytv.setText("");
            mPlaytv.setText("PLAY");
            mChronometer.stop();
            mChronometer.setFormat(null);
            animation.cancel();
            //mProgressBar.clearAnimation();
        }

    }

    protected Boolean checkSoundStatus(){
        String status = mPlaytv.getText().toString();
        return status == ("STOP")? true:false;
    }
    protected void MinMinusClicked(View v){
        mediDuration = new Long(parseLong(mMintv.getText().toString()));

        if(mediDuration <= 1){
            return;
        }
        mediDuration--;
        mMintv.setText("");
        mMintv.setText(String.valueOf(mediDuration));
    }

    protected void MinPlusClicked(View v){
        mediDuration = new Long(parseLong(mMintv.getText().toString()));
        mediDuration++;
        mMintv.setText("");
        mMintv.setText(String.valueOf(mediDuration));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            String title = data.getStringExtra("Title");
            int position = data.getIntExtra("Position", 1);
            soundPosition = position;
            mSoundView.setText("");
            mSoundView.setText(title);
        }
    }

    private void releaseMediaPlayer(){
        if(mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public void onChronometerTick(Chronometer chronometer) {
        String meditationTime = mMintv.getText().toString();
        String ChronometerTime = "\"0" + meditationTime + ":00\"";
        if(chronometer.getText().toString().equalsIgnoreCase(ChronometerTime)){
            mChronometer.stop();
            mChronometer.setFormat(null);
            animation.cancel();
        }
    }
}


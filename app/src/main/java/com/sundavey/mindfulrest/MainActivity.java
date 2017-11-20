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
import static java.lang.Long.valueOf;

public class MainActivity extends AppCompatActivity implements OnChronometerTickListener{

    private ImageButton playBtn, minMinus, minPlus;
    private TextView mSoundView, mPlaytv, mMintv, mMinLetter, mDuration;
    private MediaPlayer mMediaPlayer;
    private long mediDuration;
    private int soundPosition = -1;
    private ArrayList<Sound> sounds;
    private ProgressBar mProgressBar;
    private Chronometer mChronometer;
    private ObjectAnimator animation;
    private Sound mSound;
    private Boolean presetSound;

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
        sounds.add(new Sound("Wind", "48sec", R.raw.wind));
        //these will be pre set timed meditation
        sounds.add(new Sound("Body Scan for Sleep", "13:50", R.raw.body_scan_for_sleep));
        sounds.add(new Sound("Body Scan Meditation", "02:44", R.raw.body_scan_meditation));
        sounds.add(new Sound("Body Sound Meditation", "03:06", R.raw.body_sound_meditation));
        sounds.add(new Sound("Breathing Meditation", "05:31", R.raw.breating_meditation));
        sounds.add(new Sound("Breath, Sound, Body Meditation", "12:00", R.raw.breath_sound_body_meditation));
        sounds.add(new Sound("Loving Kindness Meditation", "09:31", R.raw.loving_kindness_meditation));
        sounds.add(new Sound("Working with Difficulties","06:55", R.raw.working_with_difficulties_meditation));
        sounds.add(new Sound("Complete Meditation Instruction", "19:00", R.raw.complete_meditation));

        ImageButton selectSoundBtn = (ImageButton) findViewById(R.id.selectSound);
        playBtn = (ImageButton) findViewById(R.id.playbtn);
        mMintv  = (TextView) findViewById(R.id.minuteInput);
        mSoundView = (TextView) findViewById(sound);
        mPlaytv = (TextView) findViewById(R.id.playtv);
        mProgressBar = (ProgressBar) findViewById(R.id.circularProgressBar);
        mChronometer = (Chronometer) findViewById(R.id.chronometer);
        minMinus = (ImageButton) findViewById(R.id.minMinus);
        minPlus = (ImageButton) findViewById(R.id.minPlus);
        mMinLetter = (TextView) findViewById(R.id.min);
        mDuration = (TextView) findViewById(R.id.duration);

        selectSoundBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkSoundStatus()) {
                    releaseMediaPlayer();
                    playBtn.setBackgroundResource(R.mipmap.play);
                    mPlaytv.setText("");
                    mPlaytv.setText("PLAY");
                    mChronometer.stop();
                    animation.cancel();
                }
                Intent intent = new Intent(MainActivity.this, SelectSound.class);
                startActivityForResult(intent, 1);
            }
        });

        mChronometer.setOnChronometerTickListener(new OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                String meditationTime = mMintv.getText().toString();
                String ChronometerTime;
                if (presetSound) {
                    meditationTime = mMinLetter.getText().toString();
                    ChronometerTime = String.valueOf(meditationTime);
                    Log.d("length", ChronometerTime);
                }else{
                    meditationTime = mMintv.getText().toString();
                    ChronometerTime = "\"0" + meditationTime + ":00\"";
                }
                if(chronometer.getText().toString().equalsIgnoreCase(ChronometerTime)){
                    mChronometer.stop();
                    mChronometer.setFormat(null);
                    releaseMediaPlayer();
                    mSound = sounds.get(0);
                    mMediaPlayer = MediaPlayer.create(MainActivity.this, mSound.getSoundResourceId());
                    mMediaPlayer.start();
                    mMediaPlayer.setOnCompletionListener(mCompleteListner);
                    playHelper();
                }
            }
        });
    }

    protected void StartBtnClicked(View v){
        String mediduration;
        long mChronometerTime;

        if(presetSound){
            mChronometerTime = timeConverter(mMinLetter.getText().toString());

        }else{
            mediduration = mMintv.getText().toString();
            mChronometerTime = valueOf(mediduration).intValue() * 60000;
        }

        if(soundPosition == -1){
            mSound = sounds.get(0);
        }else{
            mSound = sounds.get(soundPosition);
        }
        //true for PLAYing status false for paused status
        Boolean soundPlaying = checkSoundStatus();
        //when ready to play status
        if(!soundPlaying) {
            mMediaPlayer = MediaPlayer.create(MainActivity.this, mSound.getSoundResourceId());
            mMediaPlayer.start();
            mMediaPlayer.setOnCompletionListener(mCompleteListner);
            mChronometer.setBase(SystemClock.elapsedRealtime());
            mChronometer.start();
            stopHelper();
            animation = ObjectAnimator.ofInt(mProgressBar, "progress", 0, 1000);
            animation.setDuration(mChronometerTime);
            animation.setInterpolator(new DecelerateInterpolator());
            animation.start();
        }else{
            //user clicked on pause button so let the button back to "PLAY"
            playHelper();
            releaseMediaPlayer();
            mChronometer.stop();
            mChronometer.setFormat(null);
            animation.cancel();
        }
    }

    protected void playHelper(){
        playBtn.setBackgroundResource(R.mipmap.play);
        mPlaytv.setText("" + "PLAY");
    }

    protected void stopHelper(){
        playBtn.setBackgroundResource(stop);
        mPlaytv.setText("" + "STOP");
    }

    protected Boolean checkSoundStatus(){
        String status = mPlaytv.getText().toString();
        return status == ("STOP")? true:false;
    }
    protected void MinMinusClicked(View v){
        mediDuration = Long.valueOf(mMintv.getText().toString());
        if(mediDuration <= 1){
            return;
        }
        mediDuration--;
        mMintv.setText("" + String.valueOf(mediDuration));
    }

    protected void MinPlusClicked(View v){
        mediDuration = Long.valueOf(mMintv.getText().toString());
        mediDuration++;
        mMintv.setText("" + String.valueOf(mediDuration));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            String title = data.getStringExtra("Title");
            soundPosition = data.getIntExtra("Position", 1);
            if(soundPosition > 4){
                Sound currentSound = sounds.get(soundPosition);
                String soundLength = currentSound.getSoundLength();
                mMintv.setVisibility(View.GONE);
                mMinLetter.setText("" + soundLength);
                minMinus.setVisibility(View.GONE);
                minPlus.setVisibility(View.GONE);
                mDuration.setText("" + "Duration");
                presetSound = true;
            }
            else{
                mMintv.setVisibility(View.VISIBLE);
                mMinLetter.setText("Min");
                minMinus.setVisibility(View.VISIBLE);
                minPlus.setVisibility(View.VISIBLE);
                mDuration.setText("Set Duration");
                presetSound = false;
            }
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
        String ChronometerTime;
        if (presetSound) {
            ChronometerTime = String.valueOf(mMinLetter);
        }else{
            ChronometerTime = "\"0" + meditationTime + ":00\"";
        }
        if(chronometer.getText().toString().equalsIgnoreCase(ChronometerTime)){
            mChronometer.stop();
            mChronometer.setFormat(null);
            animation.cancel();
        }
    }

    protected Long timeConverter(String time){
        String[] seperated = time.split(":");

        long min = Integer.parseInt(seperated[0]);
        long sec = Integer.parseInt(seperated[1]);

        long minToMillisec = min * 60000;
        long secToMillisec = sec * 1000;

        long totalMillisec = minToMillisec + secToMillisec;
        return totalMillisec;
    }
}


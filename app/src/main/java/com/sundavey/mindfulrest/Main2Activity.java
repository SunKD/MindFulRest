package com.sundavey.mindfulrest;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {

    private Button startBtn, selectSoundBtn;
    private TextView mTextView;
    private MediaPlayer mMediaPlayer;
    private int MediDuration = 3;
    private int soundPosition = -1;
    private ArrayList<Sound> sounds;


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

        startBtn = (Button) findViewById(R.id.startbtn);
        selectSoundBtn = (Button) findViewById(R.id.selectSound);
        mTextView = (TextView) findViewById(R.id.textView);


        selectSoundBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main2Activity.this, SelectSound.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    protected void StartBtnClicked(View v){
        Sound sound;
        if(soundPosition == -1){
            sound = sounds.get(0);
            releaseMediaPlayer();
            mMediaPlayer = MediaPlayer.create(Main2Activity.this, sound.getSoundResourceId());
            mMediaPlayer.start();
            mMediaPlayer.setOnCompletionListener(mCompleteListner);
        }else{
            sound = sounds.get(soundPosition);
            releaseMediaPlayer();
            mMediaPlayer = MediaPlayer.create(Main2Activity.this, sound.getSoundResourceId());
            mMediaPlayer.start();
            mMediaPlayer.setOnCompletionListener(mCompleteListner);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            String title = data.getStringExtra("Title");
            int position = data.getIntExtra("Position", 1);
            soundPosition = position;
            mTextView.setText("Current Sound: \n\n" +title);
        }
    }

    private void releaseMediaPlayer(){
        if(mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

}


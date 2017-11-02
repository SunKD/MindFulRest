package com.sundavey.mindfulrest;

/**
 * Created by SunDa on 10/26/2017.
 */

public class Sound {
    private String mSoundName;

    private String mSoundLength;

    private int mSoundResourceId;


    public Sound(String soundName, String soundLength, int soundId){
        this.mSoundName = soundName;
        this.mSoundLength = soundLength;
        this.mSoundResourceId = soundId;
    }

    public String getSoundName(){
        return mSoundName;
    }

    public String getSoundLength(){
        return mSoundLength;
    }

    public int getSoundResourceId(){
        return mSoundResourceId;
    }
}

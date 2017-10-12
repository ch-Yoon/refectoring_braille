package com.project.why.braillelearning.Module;

import android.content.Context;
import android.media.MediaPlayer;

import com.project.why.braillelearning.R;

/**
 * Created by hyuck on 2017-10-11.
 */

public class MediaPlayerSingleton {
    private int NONE = -1;
    private Context context;
    private MediaPlayer basicMediaPlayer;
    private MediaPlayer effectMediaPlyaer;
    private int FingerSound[] = {R.raw.next,R.raw.previous};

    private static final MediaPlayerSingleton ourInstance = new MediaPlayerSingleton();

    public static MediaPlayerSingleton getInstance() {
        return ourInstance;
    }

    private MediaPlayerSingleton() {
    }

    public void setContext(Context context){
        this.context = context;
    }

    public void SoundPlay(int index, int soundId){
        InitMediaPlayer();
        fingerSoundPlay(index, soundId);
    }

    public void SoundPlay(int index, String resName){
        String packName = context.getPackageName();
        int soundId =  context.getResources().getIdentifier(resName,"raw",packName);
        InitMediaPlayer();
        fingerSoundPlay(index, soundId);
    }

    public void SoundPlay(int SoundId){
        if(SoundId != NONE) {
            InitMediaPlayer();
            basicMediaPlayer = MediaPlayer.create(context, SoundId);
            basicMediaPlayer.start();
            basicMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    InitMediaPlayer();
                }
            });
        }
    }

    public void fingerSoundPlay(int index, final int soundId){
        if(index != NONE) {
            if (index < FingerSound.length) {
                basicMediaPlayer = MediaPlayer.create(context, FingerSound[index]);
                basicMediaPlayer.start();
                basicMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        SoundPlay(soundId);
                    }
                });
            } else {
                SoundPlay(soundId);
            }
        } else {
            SoundPlay(soundId);
        }
    }

    public void effectSoundPlay(int soundId){
        if(soundId != NONE){
            if(effectMediaPlyaer == null) {
                effectMediaPlyaer = MediaPlayer.create(context, soundId);
                effectMediaPlyaer.start();
                effectMediaPlyaer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        InitMediaPlayer();
                    }
                });
            }
        }
    }

    public void InitMediaPlayer(){
        if(basicMediaPlayer != null){
            if(basicMediaPlayer.isPlaying()){
                basicMediaPlayer.stop();
                basicMediaPlayer.release();
                basicMediaPlayer = null;
            } else {
                basicMediaPlayer.release();
                basicMediaPlayer = null;
            }
        }

        if(effectMediaPlyaer != null){
            if(effectMediaPlyaer.isPlaying()){
                effectMediaPlyaer.stop();
                effectMediaPlyaer.release();
                effectMediaPlyaer = null;
            } else {
                effectMediaPlyaer.release();
                effectMediaPlyaer = null;
            }
        }
    }
}

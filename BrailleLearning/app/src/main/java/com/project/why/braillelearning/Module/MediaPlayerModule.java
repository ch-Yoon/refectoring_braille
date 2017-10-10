package com.project.why.braillelearning.Module;

import android.content.Context;
import android.media.MediaPlayer;

import com.project.why.braillelearning.R;

/**
 * Created by hyuck on 2017-09-04.
 */

public class MediaPlayerModule {
    private int NONE = -1;
    private Context mContext;
    private MediaPlayer basicMediaPlayer;
    private MediaPlayer effectMediaPlyaer;
    private int FingerSound[] = {R.raw.next,R.raw.previous};

    public MediaPlayerModule(Context context){
        mContext = context;
    }

    public void SoundPlay(int index, int id){
        final int SoundId = id;
        InitMediaPlayer();

        if(index != NONE) {
            if (index < FingerSound.length) {
                basicMediaPlayer = MediaPlayer.create(mContext, FingerSound[index]);
                basicMediaPlayer.start();
                basicMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        SoundPlay(SoundId);
                    }
                });
            } else
                SoundPlay(SoundId);

        } else
            SoundPlay(SoundId);
    }

    public void SoundPlay(int index, String resName){
        String packName = mContext.getPackageName();
        final int SoundId =  mContext.getResources().getIdentifier(resName,"raw",packName);

        InitMediaPlayer();

        if(index != NONE) {
            if (index < FingerSound.length) {
                basicMediaPlayer = MediaPlayer.create(mContext, FingerSound[index]);
                basicMediaPlayer.start();
                basicMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        SoundPlay(SoundId);
                    }
                });
            } else {
                SoundPlay(SoundId);
            }
        } else {
            SoundPlay(SoundId);
        }
    }

    public void SoundPlay(int SoundId){
        if(SoundId != NONE) {
            InitMediaPlayer();
            basicMediaPlayer = MediaPlayer.create(mContext, SoundId);
            basicMediaPlayer.start();
            basicMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    InitMediaPlayer();
                }
            });
        }
    }

    public void effectSoundPlay(int soundId){
        if(soundId != NONE){
            if(effectMediaPlyaer == null) {
                effectMediaPlyaer = MediaPlayer.create(mContext, soundId);
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

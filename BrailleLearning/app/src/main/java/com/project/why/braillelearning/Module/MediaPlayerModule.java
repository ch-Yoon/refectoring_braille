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
    private int FingerSound[] = {R.raw.next,R.raw.previous};
    private MediaPlayer mediaPlayer;

    public MediaPlayerModule(Context context){
        mContext = context;
    }

    public void SoundPlay(int index, int id){
        final int SoundId = id;
        InitMediaPlayer();

        if(index != NONE) {
            if (index < FingerSound.length) {
                mediaPlayer = MediaPlayer.create(mContext, FingerSound[index]);
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
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
        InitMediaPlayer();
        mediaPlayer = MediaPlayer.create(mContext, SoundId);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                InitMediaPlayer();
            }
        });
    }

    public void InitMediaPlayer(){
        if(mediaPlayer != null){
            if(mediaPlayer.isPlaying()){
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer=null;
            } else {
                mediaPlayer.release();
                mediaPlayer=null;
            }
        }
    }
}

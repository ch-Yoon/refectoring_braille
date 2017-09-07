package com.project.why.braillelearning;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;

/**
 * Created by hyuck on 2017-08-31.
 */

public class FingerFuctionSoundPool {
    private Context mContext;
    private int FingerSound[] = {R.raw.next,R.raw.previous};
    private MediaPlayer mediaPlayer;

    FingerFuctionSoundPool(Context context){
        mContext = context;
    }

    public void SoundPlay(int index){
        if(mediaPlayer==null) {
            mediaPlayer = MediaPlayer.create(mContext, FingerSound[index]);
            mediaPlayer.start();
        } else {
            if(mediaPlayer.isPlaying()){
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer=null;
                } else {
                    mediaPlayer.release();
                    mediaPlayer=null;
                }

                mediaPlayer = MediaPlayer.create(mContext, FingerSound[index]);
                mediaPlayer.start();
            }
        }
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(mediaPlayer!=null){
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
        });
    }


}

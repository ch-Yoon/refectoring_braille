package com.project.why.braillelearning.LearningControl;

import android.content.Context;
import android.os.Vibrator;

import com.project.why.braillelearning.EnumConstant.DotType;
import com.project.why.braillelearning.EnumConstant.Vibrate;
import com.project.why.braillelearning.LearningModel.BasicLearningCoordinate;
import com.project.why.braillelearning.LearningModel.BasicLearningData;
import com.project.why.braillelearning.Module.MediaPlayerSingleton;
import com.project.why.braillelearning.R;

/**
 * Created by User on 2017-10-09.
 */

public class BasicOneFinger implements OneFingerFunction {
    private Vibrator vibrator;
    private MediaPlayerSingleton mediaPlayerModule;
    private int previous_i;
    private int previous_j;

    BasicOneFinger(Context context){
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        mediaPlayerModule = MediaPlayerSingleton.getInstance();
        mediaPlayerModule.setContext(context);
    }

    @Override
    public void initCoordinate(){
        previous_i = 0;
        previous_j = 0;
    }

    public void oneFingerFunction(BasicLearningData data, float downX, float downY){
        BasicLearningCoordinate coordinateXY[][] = data.getCoordinate_XY();
        int col = coordinateXY.length;
        int row = coordinateXY[0].length;
        float x = downX;
        float y = downY;

        if(data.checkCoordinateInside(x, y)){
            for (int i = 0; i < col; i++) {
                for (int j = 0; j < row; j++) {
                    BasicLearningCoordinate targetCoordinate = coordinateXY[i][j];
                    if (targetCoordinate.checkSatisfyArea_Y(y)) {
                        if (targetCoordinate.checkSatisfyArea_X(x)) {
                            int dotType = targetCoordinate.getDotType();

                            if(targetCoordinate.getTarget()){
                                if(i != previous_i || j != previous_j)
                                    startEffect(dotType, true);
                            } else {
                                if(dotType == DotType.EXTERNAL_WALL.getNumber() || dotType == DotType.DEVISION_LINE.getNumber())
                                    startEffect(dotType, false);
                                else{
                                    if(i != previous_i || j != previous_j)
                                        startEffect(dotType, false);
                                }
                            }
                            previous_i = i;
                            previous_j = j;
                        }
                    } else
                        break;
                }
            }
        }
    }

    public void startEffect(int dotType, boolean target){
        startMediaPlayer(dotType, target);
        startVibration(target);
    }

    public void startMediaPlayer(int dotType, boolean target){
        int targetSoundId[] = {R.raw.men_1, R.raw.men_2, R.raw.men_3, R.raw.men_4, R.raw.men_5, R.raw.men_6};
        int nonTargetSoundId[] = {R.raw.women_1, R.raw.women_2, R.raw.women_3, R.raw.women_4, R.raw.women_5, R.raw.women_6, R.raw.external_wall, R.raw.devision_line};

        if(target == true) {
            mediaPlayerModule.SoundPlay(targetSoundId[dotType-1]);
        } else {
            if(dotType <= DotType.SIX_DOT.getNumber())
                mediaPlayerModule.SoundPlay(nonTargetSoundId[dotType-1]);
            else
                mediaPlayerModule.effectSoundPlay(nonTargetSoundId[dotType-1]);
        }
    }

    public void startVibration(boolean target){
        if(target == true)
            vibrator.vibrate(Vibrate.STRONG.getStrength());
        else
            vibrator.vibrate(Vibrate.WEAKLY.getStrength());
    }
}

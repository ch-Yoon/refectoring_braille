package com.project.why.braillelearning.LearningControl;

import com.project.why.braillelearning.EnumConstant.BrailleLength;
import com.project.why.braillelearning.EnumConstant.DotType;
import com.project.why.braillelearning.Global;
import com.project.why.braillelearning.LearningModel.BasicLearningData;
import com.project.why.braillelearning.LearningModel.BrailleData;

/**
 * Created by hyuck on 2017-10-06.
 */

public class BrailleMatrixTranslationModule {
    private float BigCircleRadiusRatio;
    private float MiniCircleRadiusRatio;
    private float bigCircleRadius;
    private float miniCircleRadius;

    BrailleMatrixTranslationModule(BrailleLength brailleLength){
        initBrailleRatio(brailleLength);
    }

    public void initBrailleRatio(BrailleLength brailleLength){
        switch(brailleLength){
            case SHORT:
                BigCircleRadiusRatio = (float) 0.1;
                MiniCircleRadiusRatio = BigCircleRadiusRatio / 5;
                break;
            case LONG:
                BigCircleRadiusRatio = (float) 0.042; //
                MiniCircleRadiusRatio = BigCircleRadiusRatio / 5;
                break;
        }

        miniCircleRadius = Global.DisplayY*MiniCircleRadiusRatio;
        bigCircleRadius = Global.DisplayY*BigCircleRadiusRatio;
    }

    public BasicLearningData translationBrailleMatrix(BrailleData brailleData) {
        String letterName = brailleData.getLetterName();
        int brailleMatrix[][] = brailleData.getBrailleMatrix();
        String assistanceLetterName = brailleData.getAssistanceLetterName();
        String rawId = brailleData.getRawId();

        BasicLearningData data = new BasicLearningData(letterName, brailleMatrix, assistanceLetterName, rawId, bigCircleRadius);

        int col = brailleMatrix.length; // 점자는 3행으로 이루어짐 + 경고벽 1행
        int row = brailleMatrix[0].length;
        int dotType = 0;
        int brailleNumber = 1;
        for (int i = 0; i < row; i++) {
            float coordinate_X = getCoordinate_X(i); // Target 점자의 X 좌표값 setting
            for (int j = 0; j < col; j++) {
                float coordinate_Y = getCoordinate_Y((col-1) - j); // Target 점자의 Y 좌표값 setting
                int targetBraille = brailleMatrix[j][i]; // 점자 행렬 (0:비돌출, 1:돌출, -1:구분선, -2:경고벽)

                if(targetBraille == DotType.EXTERNAL_WALL.getNumber())
                    dotType = DotType.EXTERNAL_WALL.getNumber();
                else if(targetBraille == DotType.DEVISION_LINE.getNumber())
                    dotType = DotType.DEVISION_LINE.getNumber();
                else {
                    dotType = brailleNumber++;

                    if(DotType.SIX_DOT.getNumber() < brailleNumber)
                        brailleNumber = DotType.ONE_DOT.getNumber();
                }

                data.setCoordinate(j, i, coordinate_X ,coordinate_Y, targetBraille, dotType);
            }
        }

        return data;
    }

    public float getCoordinate_Y(int col){ // 1 - (점자 원의 크기 * 행(점자 갯수) + 점자의 반지름) = 점자의 좌표
        return Global.DisplayY * (1 - ((2 * BigCircleRadiusRatio * col) + BigCircleRadiusRatio));
    }

    public float getCoordinate_X(int row){ // 점자의 띄어쓰기 + 점자 원의 크기 * 열(점자 갯수) + 점자의 반지름 = 점자의 좌표
        return Global.DisplayY * ((2 * BigCircleRadiusRatio * row) + BigCircleRadiusRatio);
    }

    public float getBigCircleRadius(){
        return bigCircleRadius;
    }

    public float getMiniCircleRadius(){
        return miniCircleRadius;
    }
}

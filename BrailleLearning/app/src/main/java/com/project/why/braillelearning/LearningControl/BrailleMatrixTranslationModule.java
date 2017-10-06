package com.project.why.braillelearning.LearningControl;

import com.project.why.braillelearning.EnumConstant.BrailleLength;
import com.project.why.braillelearning.Global;
import com.project.why.braillelearning.LearningModel.BrailleData;

/**
 * Created by hyuck on 2017-10-06.
 */

public class BrailleMatrixTranslationModule {
    private Data data;
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
                BigCircleRadiusRatio = (float) 0.042; // 최대 21개의 점 공간이 필요함. 1/21
                MiniCircleRadiusRatio = BigCircleRadiusRatio / 5;
                break;
        }
        miniCircleRadius = Global.DisplayY*MiniCircleRadiusRatio;
        bigCircleRadius = Global.DisplayY*BigCircleRadiusRatio;
    }

    public void translationBrailleMatrix(BrailleData brailleData) {
        int EXTERNAL_WALL = -2;
        int DIVISION_LINE = -1;
        String letterName = brailleData.getLetterName();
        int brailleMatrix[][] = brailleData.getBrailleMatrix();

        int col = brailleMatrix.length; // 점자는 3행으로 이루어짐 + 경고벽 1행
        int row = brailleMatrix[0].length;
        int brailleNumber = 1;
        int dotType = 0;

        data = null;
        data = new Data(col, row, letterName, bigCircleRadius);



        for (int i = 0; i < row; i++) {
            float coordinate_X = getCoordinate_X(i); // Target 점자의 X 좌표값 setting
            for (int j = 0; j < col; j++) {
                float coordinate_Y = getCoordinate_Y((col-1) - j); // Target 점자의 Y 좌표값 setting
                int targetBraille = brailleMatrix[j][i]; // 점자 행렬 (0:비돌출, 1:돌출, -1:구분선, -2:경고벽)

                if(targetBraille == EXTERNAL_WALL)
                    dotType = EXTERNAL_WALL;
                else if(targetBraille == DIVISION_LINE)
                    dotType = DIVISION_LINE;
                else {
                    dotType = brailleNumber;
                    if(brailleNumber+1 <= 6)
                        brailleNumber++;
                    else
                        brailleNumber = 1;
                }

                data.setCoordinate(j, i, coordinate_X ,coordinate_Y, targetBraille, dotType);
            }
        }
    }

    public float getCoordinate_Y(int col){ // 1 - (점자 원의 크기 * 행(점자 갯수) + 점자의 반지름) = 점자의 좌표
        return Global.DisplayY * (1 - ((2 * BigCircleRadiusRatio * col) + BigCircleRadiusRatio));
    }

    public float getCoordinate_X(int row){ // 점자의 띄어쓰기 + 점자 원의 크기 * 열(점자 갯수) + 점자의 반지름 = 점자의 좌표
        return Global.DisplayY * ((2 * BigCircleRadiusRatio * row) + BigCircleRadiusRatio);
        //Global.DisplayY*Space*(row/2) +
    }

    public float getBigCircleRadius(){
        return bigCircleRadius;
    }

    public float getMiniCircleRadius(){
        return miniCircleRadius;
    }

    public Data getData(){
        return data;
    }
}

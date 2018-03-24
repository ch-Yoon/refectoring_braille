package com.project.why.braillelearning.Module;

import com.project.why.braillelearning.EnumConstant.BrailleLearningType;
import com.project.why.braillelearning.EnumConstant.Database;
import com.project.why.braillelearning.EnumConstant.DotType;
import com.project.why.braillelearning.Global;
import com.project.why.braillelearning.LearningModel.Dot;

/**
 * Created by hyuck on 2017-10-06.
 */

/**
 * brailleData에 저장될 정보를 가공하는 class
 * 점자의 행, 열, 점자의 크기, 터치 영역을 설정함
 */
public class DataConversionModule {
    private float bigCircleRadiusRatio;
    private float miniCircleRadiusRatio;
    private float touchAreaRidus;

    public DataConversionModule(){}

    public DataConversionModule(Database databaseFileName){
        initBrailleRatio(databaseFileName);
    }

    public DataConversionModule(BrailleLearningType brailleLearningType){
        initBrailleRatio(brailleLearningType);
    }

    private void initBrailleRatio(Database databaseFileName) {
        switch(databaseFileName){
            case BASIC:
                bigCircleRadiusRatio = (float) 0.1;
                miniCircleRadiusRatio = bigCircleRadiusRatio / 5;
                break;
            default:
                bigCircleRadiusRatio = (float) 0.042; //
                miniCircleRadiusRatio = bigCircleRadiusRatio / 5;
                break;
        }
        touchAreaRidus = Global.displayY * bigCircleRadiusRatio;
    }

    private void initBrailleRatio(BrailleLearningType brailleLearningType) {
        switch(brailleLearningType){
            case BASIC:
                bigCircleRadiusRatio = (float) 0.1;
                miniCircleRadiusRatio = bigCircleRadiusRatio / 5;
                break;
            default:
                bigCircleRadiusRatio = (float) 0.042; //
                miniCircleRadiusRatio = bigCircleRadiusRatio / 5;
                break;
        }
        touchAreaRidus = Global.displayY * bigCircleRadiusRatio;
    }

    public String getConversionQuizRawId(Dot brailleMatrix[][], int quizCount){
        String rawId = "";
        switch(quizCount){
            case 0 :
                rawId += "firstquiz";
                break;
            case 1:
                rawId += "secondquiz";
                break;
            default :
                rawId += "lastquiz";
                break;
        }

        rawId += ",quizinfo";
        return getConversionRawId(brailleMatrix, rawId);
    }

    /**
     * 점자 행렬정보를 문자열로 분해함
     * 초성 니은을 의미하는 점자는 한 칸중, 1점과 4점이 돌출되어 있음.
     * 초성 니은의 경우, 초성 니은,일,1,4점 이라는 text로 변환됨
     * @param brailleMatrix : 점자 행렬
     * @param rawId : 글자를 출력하는 음성 file id
     * @return 변환된 음성 file string
     */
    public String getConversionRawId(Dot brailleMatrix[][], String rawId){
        String tempLetterId = rawId;
        int COL = brailleMatrix.length; //4행
       // int ROW = brailleMatrix[0].length;
        int dotCount = getDotCount(brailleMatrix);
        int ROW = dotCount * 3;
        switch (dotCount) {
            case 1:
                rawId += ",일";
                break;
            case 2:
                rawId += ",이";
                break;
            case 3:
                rawId += ",삼";
                break;
            case 4:
                rawId += ",사";
                break;
            case 5:
                rawId += ",오";
                break;
            case 6:
                rawId += ",육";
                break;
            case 7:
                rawId += ",칠";
                break;
            default:
                break;
        }

        if(0 < dotCount) {
            int cellCount = 0;
            for (int i = 0; i < ROW; i++) {
                for (int j = 1; j < COL; j++) {
                    boolean target = brailleMatrix[j][i].getTarget();
                    if (target == true) {
                        int dotType = brailleMatrix[j][i].getDotType();
                        rawId += "," + dotType;
                        cellCount++;
                    }
                }

                if (i % 3 == 2) {
                    if(rawId.length() != 0) {
                        if(cellCount == 0){
                            rawId += ",emptyspace";
                        } else {
                            char dotType[] = {'1', '2', '3', '4', '5', '6'};
                            char target = rawId.charAt(rawId.length() - 1);
                            for (int k = 0; k < dotType.length; k++) {
                                if (target == dotType[k]) {
                                    rawId += "점";
                                    break;
                                }
                            }
                        }

                        cellCount = 0;
                    }
                }
            }
        }


        if(rawId.length() == 0)
            rawId = "noinput";
        else {
            if((tempLetterId.length() == 0) && (0 < dotCount))
                rawId = "noletter"+rawId;
        }

        return rawId;
    }


    /**
     * 점자 칸 수를 반환하는 함수
     * @param brailleMatrix : 점자 정보
     * @return : 점자 칸 수
     */
    public int getDotCount(Dot[][] brailleMatrix){
        int COL = brailleMatrix.length; //4행
        int ROW = brailleMatrix[0].length;
        int dotCount = 0;
        boolean check = false;
        for(int i=0 ; i<ROW ; i++){
            for(int j=0 ; j<COL ; j++){
                if(brailleMatrix[j][i].getTarget() == true)
                    check = true;
            }

            if(i%3 == 2){
                if(check == true) {
                    //dotCount++;
                    dotCount = i/3 + 1;
                    check = false;
                }
            }
        }

        return dotCount;
    }


    /**
     * 점자 정보를 수신하면, 해당 점자 정보를 string으로 변환하는 함수
     * @param brailleMatrix : 점자정보 행렬
     * @return : string으로 구성된 점자정보
     */
    public String getConversionBrailleText(Dot[][] brailleMatrix){
        int COL = brailleMatrix.length;
        String brailleText = "";
        int realRow = getRowLength(brailleMatrix);
        for(int i=0 ; i<COL ; i++){
            for(int j=0 ; j<realRow ; j++){
                Dot dot = brailleMatrix[i][j];
                int dotType = dot.getDotType();
                if(dotType != DotType.DEVISION_LINE.getNumber() && dotType != DotType.EXTERNAL_WALL.getNumber()){
                    boolean target = dot.getTarget();
                    if(target == true)
                        brailleText += "1";
                    else
                        brailleText += "0";
                }
            }
        }
        return brailleText;
    }


    public int getRowLength(Dot[][] brailleMatrix){
        int col = brailleMatrix.length;
        int row = brailleMatrix[0].length;
        int targetRow = 0;

        for(int i=0 ; i<row ; i++){
            for(int j=0 ; j<col ; j++){
                if(brailleMatrix[j][i].getTarget()){
                    targetRow = i;
                    break;
                }
            }
        }

        if(targetRow != 0)
            targetRow = ((targetRow/3)+1)*3;

        return targetRow;
    }



    /**
     * string 형식으로 저장되어 있는 점자 행렬 정보를 가공하는 함수
     * @param brailleText : 점자 행렬정보가 string으로 저장되어 있음
     * @return : 가공된 점자 행렬
     */
    public Dot[][] getConversionBrailleMatrix(String brailleText) { // 점자를 의미하는 행렬 셋팅
        int COL = 4; //4행
        int ROW = (brailleText.length() / 3) + ((brailleText.length() / 3) / 2);
        Dot brailleMatrix[][] = new Dot[COL][ROW];

        int index=0;
        for(int i=0 ; i<COL ; i++){
            for(int j=0 ; j<ROW ; j++){
                int dotType;
                boolean target = false;

                if(i == 0)
                    dotType = DotType.EXTERNAL_WALL.getNumber();
                else {
                    if(j == ROW-1)
                        dotType = DotType.EXTERNAL_WALL.getNumber();
                    else {
                        if(j%3 == 2)
                            dotType = DotType.DEVISION_LINE.getNumber();
                        else {
                            if(j%3 == 0)
                                dotType = i;  // 1 2 3
                            else
                                dotType = i+3; // 4 5 6

                            if((int)brailleText.charAt(index++)-'0' == 1)
                                target = true;
                            else
                                target = false;
                        }
                    }
                }

                brailleMatrix[i][j] = new Dot();
                brailleMatrix[i][j].setTouchAreaRadius(touchAreaRidus);
                brailleMatrix[i][j].setBigCircleRadius(getViewAreaRidus(true));
                brailleMatrix[i][j].setSmallCircleRadius(getViewAreaRidus(false));
                brailleMatrix[i][j].setTarget(target);
                brailleMatrix[i][j].setDotType(dotType);
                brailleMatrix[i][j].setX(getCoordinate_X(j));
                brailleMatrix[i][j].setY(getCoordinate_Y(COL, i));
            }
        }

        return brailleMatrix;
    }


    /**
     * y좌표를 얻어오는 함수
     * @param nowCol : 현재 세로 축
     * @return : 1 - (점자 원의 크기 * ((전체 열-1) - 현재 열) + 점자의 반지름) = 점자의 세로 좌표
     */
    public float getCoordinate_Y(int COL, int nowCol){
        return Global.displayY * (1 - ((2 * bigCircleRadiusRatio * ((COL-1) - nowCol)) + bigCircleRadiusRatio));
    }


    /**
     * x좌표를 얻어오는 함수
     * @param row : 현재 가로 축
     * @return : 점자의 띄어쓰기 + 점자 원의 크기 * 열(점자 갯수) + 점자의 반지름 = 점자의 가로 좌표
     */
    public float getCoordinate_X(int row){
        return Global.displayY * ((2 * bigCircleRadiusRatio * row) + bigCircleRadiusRatio);
    }


    /**
     * 점자를 화면에 그리기 위한 좌표를 얻어오는 함수
     * @param target : 점자의 돌출 여부
     * @return : true일 경우 큰 동그라미, false일 경우 작은 동그라미
     */
    private float getViewAreaRidus(boolean target){
        if(target == true)
            return Global.displayY*bigCircleRadiusRatio;
        else
            return Global.displayY*miniCircleRadiusRatio;
    }
}

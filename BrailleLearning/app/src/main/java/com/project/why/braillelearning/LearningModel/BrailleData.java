package com.project.why.braillelearning.LearningModel;

import com.project.why.braillelearning.EnumConstant.DotType;
import com.project.why.braillelearning.LearningControl.BrailleLearning;

/**
 * Created by hyuck on 2017-09-12.
 */

public class BrailleData {
    protected String letterName = ""; // 점자 이름
    protected int[][] brailleMatrix; // 점자 행렬
    protected String assistanceLetterName = ""; // 퀴즈메뉴를 위한 점자 보조 이름.
    protected String rawId = "";

    public BrailleData(){
    }

    public BrailleData(String letterName, String brailleMatrix, String assistanceLetterName, String rawId){
        this.letterName = letterName;
        this.brailleMatrix = setBrailleMatrix(brailleMatrix);
        this.assistanceLetterName = assistanceLetterName;
        this.rawId = rawId;
    }

    public BrailleData(String letterName, int brailleMatrix[][], String assistanceLetterName, String rawId){
        this.letterName = letterName;
        this.brailleMatrix = brailleMatrix;
        this.assistanceLetterName = assistanceLetterName;
        this.rawId = rawId;
    }

    public int[][] setBrailleMatrix(String brailleMatrix) { // 점자를 의미하는 행렬 셋팅
        int COL=4; //4행
        int ROW = (brailleMatrix.length()/3)+((brailleMatrix.length()/3)/2);
        int index=0;

        int Matrix[][] = new int[COL][ROW];
        for(int i=0 ; i<COL ; i++){
            for(int j=0 ; j<ROW ; j++){
                if(i == 0)
                    Matrix[i][j] = DotType.EXTERNAL_WALL.getNumber();
                else {
                    if(j == ROW-1)
                        Matrix[i][j] = DotType.EXTERNAL_WALL.getNumber();
                    else {
                        if((j+1)%3 == 0) {
                            Matrix[i][j] = DotType.DEVISION_LINE.getNumber();
                        } else {
                            Matrix[i][j] = (int) brailleMatrix.charAt(index++) - '0';
                        }
                    }
                }
            }
        }

        return Matrix;
    }


    public String getLetterName(){
        return letterName;
    }

    public int[][] getBrailleMatrix(){
        return brailleMatrix;
    }

    public String getAssistanceLetterName(){
        return assistanceLetterName;
    }

    public String getRawId() {
        return rawId;
    }

}

package com.project.why.braillelearning.LearningModel;

/**
 * Created by hyuck on 2017-09-12.
 */

public class BrailleData {
    private String LetterName = ""; // 점자 이름
    private int[][] BrailleMatrix; // 점자 행렬
    private String AssistanceLetterName = ""; // 퀴즈메뉴를 위한 점자 보조 이름.

    BrailleData(String LetterName, String BrailleMatrix, String AssistanceLetterName){
        this.LetterName = LetterName;
        this.BrailleMatrix = setBrailleMatrix(BrailleMatrix);
        this.AssistanceLetterName = AssistanceLetterName;
    }

    public int[][] setBrailleMatrix(String brailleMatrix) { // 점자를 의미하는 행렬 셋팅
        int EXTERNAL_WALL = -2;
        int DIVISION_LINE = -1;

        int COL=4; //4행
        int ROW = (brailleMatrix.length()/3)+((brailleMatrix.length()/3)/2);
        //int ROW=2*brailleMatrix.length()/6; //2열 * 칸수
        int index=0;

        int Matrix[][] = new int[COL][ROW];
        for(int i=0 ; i<COL ; i++){
            for(int j=0 ; j<ROW ; j++){
                if(i == 0)
                    Matrix[i][j] = EXTERNAL_WALL;
                else {
                    if(j == ROW-1)
                        Matrix[i][j] = EXTERNAL_WALL;
                    else {
                        if((j+1)%3 == 0) {
                            Matrix[i][j] = DIVISION_LINE;
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
        return LetterName;
    }

    public int[][] getBrailleMatrix(){
        return BrailleMatrix;
    }

    public String getAssistanceLetterName(){
        return AssistanceLetterName;
    }

}

package com.project.why.braillelearning.BrailleInformationFactory;

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
        int ROW=3; //3행
        int COL=2*brailleMatrix.length()/6;; //2열 * 칸수
        int index=0;

        int Matrix[][] = new int[ROW][COL];
        for(int i=0 ; i<ROW ; i++){
            for(int j=0 ; j<COL ; j++){
                Matrix[i][j] = (int)brailleMatrix.charAt(index++)-'0';
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

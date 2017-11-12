package com.project.why.braillelearning.LearningModel;

import com.project.why.braillelearning.EnumConstant.BrailleLearningType;
import com.project.why.braillelearning.Module.DataConversionModule;

/**
 * Created by hyuck on 2017-09-12.
 */

/**
 * 점자 학습 data를 관리하는 class
 * letterName : 점자를 의미하는 글자 정보 변수
 * brailleMatrix : 점자의 좌푯값, touch 영역 좌푯값, view를 위한 좌푯값을 갖고 있는 class 행렬.
 * assistanceLetterName : 음성인식을 활용할 때, 보조로 쓰이는 점자를 의미하는 글자 정보 변수
 * rawId : 점자 정보 음성출력을 위한 음성 file id변수. 음성 file id와 점자 행렬정보를 조합하여 음성출력 정보가 구성됨
 */
public class BrailleData {
    private String letterName = ""; // 점자 이름
    private Dot brailleMatrix[][]; // 점자 행렬
    private String assistanceLetterName = ""; // 퀴즈메뉴를 위한 점자 보조 이름.
    private String rawId = ""; // 점자 음성 파일

    public BrailleData(String letterName, String brailleText, String assistanceLetterName, String rawId, BrailleLearningType brailleLearningType){
        DataConversionModule conversionModule = new DataConversionModule(brailleLearningType, brailleText); // 학습에 사용 될 수 있도록 점자 data를 가공하는 class
        this.letterName = letterName;
        this.brailleMatrix = conversionModule.getConversionBrailleMatrix(brailleText);
        this.assistanceLetterName = assistanceLetterName;
        this.rawId = conversionModule.getConversionRawId(brailleMatrix, rawId);
    }

    public BrailleData(String letterName, Dot brailleMatrix[][], String assistanceLetterName, String rawId){
        this.letterName = letterName;
        this.brailleMatrix = brailleMatrix;
        this.assistanceLetterName = assistanceLetterName;
        this.rawId = rawId;
    }

    public String getLetterName(){
        return letterName;
    }

    public String getAssistanceLetterName(){
        return assistanceLetterName;
    }

    public String getRawId() {
        return rawId;
    }

    public Dot[][] getBrailleMatrix() {
        return brailleMatrix;
    }

    /**
     * touch된 손가락의 좌표가 점자 영역 안쪽인지, 바깥쪽인지 check하는 함수
     * @param x : 손가락 1개의 x 좌표
     * @param y : 손가락 1개의 y 좌표
     * @return true(점자 영역 안쪽), false(점자 영역 아님)
     */
    public boolean checkCoordinateInside(float x, float y){
        int row = brailleMatrix[0].length;
        float maxY = brailleMatrix[0][0].getY() - brailleMatrix[0][0].getTouchAreaRidus();
        float maxX = brailleMatrix[0][row-1].getX() + brailleMatrix[0][row-1].getTouchAreaRidus();
        if(maxY <= y && x <= maxX)  // 터치 좌표가 점자 영역 안쪽일경우
            return true;
        else
            return false;
    }

}

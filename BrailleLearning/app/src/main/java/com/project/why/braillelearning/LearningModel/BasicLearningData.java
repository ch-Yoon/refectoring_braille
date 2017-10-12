package com.project.why.braillelearning.LearningModel;

import com.project.why.braillelearning.EnumConstant.DotType;

/**
 * Created by hyuck on 2017-10-05.
 */

public class BasicLearningData {
    private String letterName;
    private BasicLearningCoordinate coordinate_XY[][];
    private String rawId;
    private float radius;

    public BasicLearningData(int col, int row, String letterName, String rawId, float bigCircleRadius){
        this.letterName = letterName;
        this.rawId = rawId;
        this.coordinate_XY = new BasicLearningCoordinate[col][row];
        this.radius = bigCircleRadius;
    }

    public void setCoordinate(int col, int row, float x, float y, int targetBraille, int dotType){
        if(coordinate_XY[col][row] == null)
            coordinate_XY[col][row] = new BasicLearningCoordinate(radius);

        coordinate_XY[col][row].setX(x);
        coordinate_XY[col][row].setY(y);
        coordinate_XY[col][row].setDotType(dotType);

        if(targetBraille == DotType.PROJECTED.getNumber())
            coordinate_XY[col][row].setTarget(true);
        else
            coordinate_XY[col][row].setTarget(false);
    }

    public String getLetterName(){
        return letterName;
    }

    public String getRawId() {
        return rawId;
    }

    public BasicLearningCoordinate[][] getCoordinate_XY(){
        return coordinate_XY;
    }

    public boolean checkCoordinateInside(float x, float y){
        int row = coordinate_XY[0].length;
        if(coordinate_XY[0][0].checkInsideArea_Y(y) && coordinate_XY[0][row-1].checkInsideArea_X(x))  // 터치 좌표가 점자 영역 안쪽일경우
            return true;
        else
            return false;

    }
}

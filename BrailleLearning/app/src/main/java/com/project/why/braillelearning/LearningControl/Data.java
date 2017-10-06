package com.project.why.braillelearning.LearningControl;

import com.project.why.braillelearning.LearningControl.Coordinate;

/**
 * Created by hyuck on 2017-10-05.
 */

public class Data {
    private String letterName;
    private Coordinate coordinate_XY[][];
    private float radius;

    Data(int col, int row, String letterName, float bigCircleRadius){
        this.letterName = letterName;
        this.coordinate_XY = new Coordinate[col][row];
        this.radius = bigCircleRadius;
    }

    public void setCoordinate(int col, int row, float x, float y, int targetBraille, int dotType){
        if(coordinate_XY[col][row] == null)
            coordinate_XY[col][row] = new Coordinate(radius);

        coordinate_XY[col][row].setX(x);
        coordinate_XY[col][row].setY(y);
        coordinate_XY[col][row].setDotType(dotType);

        if(targetBraille == 0)
            coordinate_XY[col][row].setTarget(false);
        else
            coordinate_XY[col][row].setTarget(true);

    }

    public String getLetterName(){
        return letterName;
    }

    public Coordinate[][] getCoordinate_XY(){
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

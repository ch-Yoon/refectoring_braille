package com.project.why.braillelearning.EnumConstant;

/**
 * Created by User on 2017-10-07.
 */


/**
 * 점 종류 enum
 * EXTERNAL_WALL : 경고벽
 * DEVISION_LINE : 구분선
 */
public enum DotType {
    EXTERNAL_WALL(7), DEVISION_LINE(8);

    int number;
    DotType(int number){
        this.number = number;
    }

    public int getNumber(){
        return number;
    }
}

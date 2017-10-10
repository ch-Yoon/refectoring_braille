package com.project.why.braillelearning.EnumConstant;

/**
 * Created by User on 2017-10-07.
 */

public enum DotType {
    ONE_DOT(1), TWO_DOT(2), THREE_DOT(3), FOUR_DOT(4), FIVE_DOT(5), SIX_DOT(6), EXTERNAL_WALL(7), DEVISION_LINE(8),
    NON_PROJECTED(0), PROJECTED(1);

    int number;
    DotType(int number){
        this.number = number;
    }

    public int getNumber(){
        return number;
    }
}

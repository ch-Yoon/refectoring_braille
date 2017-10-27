package com.project.why.braillelearning.EnumConstant;

/**
 * Created by User on 2017-10-09.
 */

public enum FingerFunctionType {
    NONE(-1), NEXT(0), PREVIOUS(1), ENTER(2), BACK(3), SPECIAL(4), TRANSLATION(5),
    ONE_FINGER(1), TWO_FINGER(2), THREE_FINGER(3);

    int number;

    FingerFunctionType(int number){
        this.number = number;
    }

    public int getNumber(){
        return number;
    }
}

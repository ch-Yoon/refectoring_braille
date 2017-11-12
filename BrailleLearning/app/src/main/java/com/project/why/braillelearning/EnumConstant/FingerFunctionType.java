package com.project.why.braillelearning.EnumConstant;

/**
 * Created by User on 2017-10-09.
 */

public enum FingerFunctionType {
    NONE(-1), RIGHT(0), LEFT(1), ENTER(2), BACK(3), REFRESH(4), SPEECH(5), MYNOTE(6),
    ONE_FINGER(1), TWO_FINGER(2), THREE_FINGER(3);

    int number;

    FingerFunctionType(int number){
        this.number = number;
    }

    public int getNumber(){
        return number;
    }
}

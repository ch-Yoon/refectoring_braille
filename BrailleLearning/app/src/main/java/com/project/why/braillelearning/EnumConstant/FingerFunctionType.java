package com.project.why.braillelearning.EnumConstant;

/**
 * Created by User on 2017-10-09.
 */

/**
 * NONE : 해당사항 없음
 * RIGHT : 오른쪽 페이지 이동
 * LEFT : 왼쪽 페이지 이동
 * ENTER : 메뉴 접속
 * REFRESH : 화면 새로고침
 * SPEECH : 음성인식
 * MYNOTE : 나만의 단어장 저장 및 삭제
 * ONE_FINGER : 손가락 1개
 * TWO_FINGER : 손가락 2개
 * THREE_FINGER : 손가락 3개
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

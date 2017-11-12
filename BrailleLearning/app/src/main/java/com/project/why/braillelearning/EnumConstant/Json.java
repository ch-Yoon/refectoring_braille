package com.project.why.braillelearning.EnumConstant;

/**
 * Created by hyuck on 2017-09-24.
 */

/**
 * 점자 data를 갖고 있는 Json File name enum
 * INITIAL : 초성
 * VOWEL : 모음
 * FINAL : 종성
 * NUMBER : 숫자
 * ALPHABET : 알파벳
 * SETENCE : 문장부호
 * ABBREVIATION : 약자 및 약어
 * LETTER : 글자
 * WORD : 단어
 * TEACHER : 선생님 모드
 */
public enum Json {
    INITIAL("InitialBrailleData"), VOWEL("VowelBrailleData"), FINAL("FinalBrailleData"), NUMBER("NumberBrailleData"), ALPHABET("AlphabetBrailleData"),
    SENTENCE("SentenceBrailleData"), ABBREVIATION("AbbreviationBrailleData"), LETTER("LetterBrailleData"), WORD("WordBrailleData"), TEACHER("TeacherBrailleData");

    private String name;
    Json(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}

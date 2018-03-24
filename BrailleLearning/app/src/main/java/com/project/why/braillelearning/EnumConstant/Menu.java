package com.project.why.braillelearning.EnumConstant;

/**
 * Created by hyuck on 2017-09-24.
 */

/**
 * Menu의 이름들을 정의해논 enum
 * TRUE는 음성인식이 사용되는 메뉴, FALSE는 음성인식이 사용되지 않는 메뉴
 * TUTORIAL : 사용설명서
 * BASIC : 기초과정
 * MASTER : 숙련과정
 * TRANSLATION : 점자번역
 * QUIZ : 퀴즈
 * MYNOTE : 나만의단어장
 * COMMUNICATION : 선생님과의대화
 * INITIAL_BASIC : 초성연습
 * VOWEL_BASIC : 모음연습
 * FINAL_BASIC : 종성연습
 * NUMBER_BASIC : 숫자연습
 * ALPHABET_BASIC : 알파벳연습
 * SENTENCE_BASIC : 문장부호연습
 * ABBREVIATION_BASIC : 약자 및 약어연습
 * LETTER_MASTER : 글자연습
 * WORD_MASTER : 단어연습
 * INITIAL_QUIZ : 초성퀴즈
 * VOWEL_QUIZ : 모음퀴즈
 * FINAL_QUIZ : 종성퀴즈
 * NUMBER_QUIZ : 숫자퀴즈
 * ALPHABET_QUIZ : 알파벳퀴즈
 * SENTENCE_QUIZ : 문장부호퀴즈
 * ABBREVIATION_QUIZ : 약자 및 약어퀴즈
 * LETTER_QUIZ : 글자퀴즈
 * WORD_QUIZ : 단어퀴즈
 * BASIC_MYNOTE : 기초단어장
 * MASTER_MYNOTE : 숙련단어장
 * COMMUNICATION_MYNOTE : 선생님의단어장
 * TEACHER_COMMUNICATION : 선생님모드
 * STUDENT_COMMUNICATION : 학생모드
 */
public enum Menu {
    TUTORIAL(false),
    BASIC(false),
    MASTER(false),
    TRANSLATION(true),
    QUIZ(true),
    MYNOTE(false),
    COMMUNICATION(true),

    INITIAL_BASIC(false),
    VOWEL_BASIC(false),
    FINAL_BASIC(false),
    NUMBER_BASIC(false),
    ALPHABET_BASIC(false),
    SENTENCE_BASIC(false),
    ABBREVIATION_BASIC(false),

    LETTER_MASTER(false),
    WORD_MASTER(false),

    INITIAL_QUIZ(true),
    VOWEL_QUIZ(true),
    FINAL_QUIZ(true),
    NUMBER_QUIZ(true),
    ALPHABET_QUIZ(true),
    SENTENCE_QUIZ(true),
    ABBREVIATION_QUIZ(true),
    LETTER_QUIZ(true),
    WORD_QUIZ(true),

    BASIC_MYNOTE(false),
    MASTER_MYNOTE(false),
    COMMUNICATION_MYNOTE(false),

    TEACHER_COMMUNICATION(true),
    STUDENT_COMMUNICATION(true);

    private boolean usePermission;


    Menu(boolean usePermission){
        this.usePermission = usePermission;
    }

    public boolean getUsePermission(){
        return usePermission;
    }
}

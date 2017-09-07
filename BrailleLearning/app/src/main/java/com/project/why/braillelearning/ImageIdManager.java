package com.project.why.braillelearning;

import java.util.Deque;
import java.util.LinkedList;

/**
 * Created by hyuck on 2017-08-29.
 */

public class ImageIdManager {
    /*
     * 메뉴에 사용될 image들을 관리하는 manager class
     */

    private final int BIGMENU_TUTORIAL = 0; // 사용설명서
    private final int BIGMENU_BASIC_PRACTICE = 1; // 기초과정
    private final int BIGMENU_MASTER_PRACTICE = 2; // 숙련과정
    private final int BIGMENU_TRANSLATION = 3; // 점자번역
    private final int BIGMENU_QUIZ = 4; // 퀴즈
    private final int BIGMENU_MYNOTE = 5; // 나만의 단어장
    private final int BIGMENU_COMMUNICATION = 6; // 선생님과의 대화

    protected ImageIdManager(){
    }

    public int[] getImageId(Deque<Integer> AdressDeque){ // 이미지를 얻기 위한 함수. Deque안에 메뉴 이미지를 찾기 위한 주소 index가 들어있음
        Deque<Integer> DequeAdress = DequeCopy(AdressDeque);

        if(DequeAdress.size()==0){ // Deque의 사이즈가 0이라면 대 메뉴
            return getBigMenu();
        } else if(DequeAdress.size()==1){ // 사이즈가 1이라면 중 메뉴
            return getMiddleMenu(DequeAdress);
        } else if(DequeAdress.size()==2){ // 사이즈가 2라면 소메뉴
            return getSmallMenu(DequeAdress);
        } else
            return null;
    }

    public int[] getBigMenu(){ // 대 메뉴의 image id 배열을 반환하는 함수
        int BigMenu[] = {R.drawable.tutorial, R.drawable.basic_practice, R.drawable.master_practice, R.drawable.brailletranslation, R.drawable.quiz, R.drawable.mynote, R.drawable.comunication};
        return BigMenu;
    }

    public int[] getMiddleMenu(Deque<Integer> AdressDeque){ // 중 메뉴의 image id 배열을 반환하는 함수
        Deque<Integer> DequeAdress = DequeCopy(AdressDeque);
        switch(DequeAdress.pollFirst()){  // 중 메뉴 삽입
            case BIGMENU_BASIC_PRACTICE:
                int MiddleBasicMenu[] = {R.drawable.initial_practice, R.drawable.vowel_practice, R.drawable.final_practice, R.drawable.number_practice, R.drawable.alphabet_practice, R.drawable.sentence_practice, R.drawable.promise_practice};
                return MiddleBasicMenu;
            case BIGMENU_MASTER_PRACTICE:
                int MiddleMasterMenu[] = {R.drawable.letter_practice, R.drawable.word_practice};
                return MiddleMasterMenu;
            case BIGMENU_QUIZ:
                int MiddleQuizMenu[] = {R.drawable.initial_quiz, R.drawable.vowel_quiz, R.drawable.final_quiz, R.drawable.number_quiz, R.drawable.alphabet_quiz, R.drawable.sentence_quiz, R.drawable.promise_quiz, R.drawable.letter_quiz, R.drawable.word_quiz};
                return MiddleQuizMenu;
            case BIGMENU_MYNOTE:
                int MiddleMynoteMenu[] = {R.drawable.mynote_basic, R.drawable.mynote_master, R.drawable.mynote_communication};
                return MiddleMynoteMenu;
            case BIGMENU_COMMUNICATION:
                int MiddleCommunicationMenu[] = {R.drawable.teacher_mode, R.drawable.student_mode};
                return MiddleCommunicationMenu;
            default:
                return null;
        }
    }

    public int[] getSmallMenu(Deque<Integer> AdressDeque){ // 소 메뉴의 image id 배열을 반환하는 함수
        Deque<Integer> DequeAdress = DequeCopy(AdressDeque);
        switch(DequeAdress.pollFirst()){
            case BIGMENU_QUIZ:
                int SmallQuizMenu[] = {R.drawable.reading_quiz, R.drawable.writing_quiz};
                return SmallQuizMenu;
            default:
                return null;
        }
    }

    public Deque<Integer> DequeCopy(Deque<Integer> Deque){
        Deque<Integer> CopyDeque = new LinkedList<>();
        CopyDeque.addAll(Deque);
        return CopyDeque;
    }
}

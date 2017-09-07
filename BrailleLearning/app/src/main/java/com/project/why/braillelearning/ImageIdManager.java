package com.project.why.braillelearning;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

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
        int BigMenu[] = {R.drawable.tutorial, R.drawable.basic_practice, R.drawable.master_practice, R.drawable.translation, R.drawable.quiz, R.drawable.mynote, R.drawable.comunication};
        return BigMenu;
    }

    public int[] getMiddleMenu(Deque<Integer> AdressDeque){ // 중 메뉴의 image id 배열을 반환하는 함수
        Deque<Integer> DequeAdress = DequeCopy(AdressDeque);
        switch(DequeAdress.pollFirst()){  // 중 메뉴 삽입
            case BIGMENU_BASIC_PRACTICE:
                int MiddleBasicMenu[] = {R.drawable.middle_init, R.drawable.middle_vowel, R.drawable.middle_last, R.drawable.middle_number, R.drawable.middle_alphabet, R.drawable.middle_sentence, R.drawable.middle_promise};
                return MiddleBasicMenu;
            case BIGMENU_MASTER_PRACTICE:
                int MiddleMasterMenu[] = {R.drawable.middle_letter, R.drawable.middle_word};
                return MiddleMasterMenu;
            case BIGMENU_QUIZ:
                int MiddleQuizMenu[] = {R.drawable.middle_init_quiz, R.drawable.middle_vowel_quiz, R.drawable.middle_last_quiz, R.drawable.middle_number_quiz, R.drawable.middle_alphabet_quiz, R.drawable.middle_sentence_quiz, R.drawable.middle_promise_quiz, R.drawable.middle_letter_quiz, R.drawable.middle_word_quiz};
                return MiddleQuizMenu;
            case BIGMENU_MYNOTE:
                int MiddleMynoteMenu[] = {R.drawable.middle_mynote_basic, R.drawable.middle_mynote_master, R.drawable.middle_mynote_communication};
                return MiddleMynoteMenu;
            case BIGMENU_COMMUNICATION:
                int MiddleCommunicationMenu[] = {R.drawable.middle_teacher, R.drawable.middle_student};
                return MiddleCommunicationMenu;
            default:
                return null;
        }
    }

    public int[] getSmallMenu(Deque<Integer> AdressDeque){ // 소 메뉴의 image id 배열을 반환하는 함수
        Deque<Integer> DequeAdress = DequeCopy(AdressDeque);
        switch(DequeAdress.pollFirst()){
            case BIGMENU_QUIZ:
                int SmallQuizMenu[] = {R.drawable.small_read, R.drawable.small_write};
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

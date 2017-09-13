package com.project.why.braillelearning.BrailleFactory;

import java.util.Deque;
import java.util.LinkedList;

/**
 * Created by hyuck on 2017-09-13.
 */

public class SimpleJsonFileNameFactory implements JsonFileNameFactory {
    /* 메뉴 구성
     * tutorial
     * basicpractice (initial, vowel, final, number, alphabet, sentence, abbreviation)
     * masterpractce (letter, word)
     * translation
     * quiz (initial_quiz, vowel_quiz, final_quiz, number_quiz, alphabet_quiz, sentence_quiz, abbreviation_quiz, letter_quiz, word_quiz)
     * mynote (basic_note, master_note, communication_note)
     * communication (teacher_mode, student_mode)
     */

    private final int BASIC_PRACTICE = 1;
    private final int INITIAL = 0;
    private final int VOWEL = 1;
    private final int FINAL = 2;
    private final int NUMBER = 3;
    private final int ALPHABET = 4;
    private final int SENTENCE = 5;
    private final int ABBREVIATION = 6;

    private final int MASTER_PRACTCE = 2;
    private final int LETTER = 0;
    private final int WORD = 1;

    private final int QUIZ = 4;
    private final int INITIAL_QUIZ = 0;
    private final int VOWEL_QUIZ = 1;
    private final int FINAL_QUIZ = 2;
    private final int NUMBER_QUIZ = 3;
    private final int ALPHABET_QUIZ = 4;
    private final int SENTENCE_QUIZ = 5;
    private final int ABBREVIATION_QUIZ = 6;
    private final int LETTER_QUIZ = 7;
    private final int WORD_QUIZ = 8;
    public SimpleJsonFileNameFactory(){

    }

    @Override
    public GettingJsonFileName getJsonFileNameObject(Deque<Integer> Deque) {
        Deque<Integer> MenuAdreeDeque = new LinkedList<>();
        MenuAdreeDeque.addAll(Deque);
        int BigMenuNumber = getMenuNumber(MenuAdreeDeque);
        int MiddleMenuNumber = getMenuNumber(MenuAdreeDeque);

        switch(BigMenuNumber){
            case BASIC_PRACTICE:
                switch(MiddleMenuNumber){
                    case INITIAL:
                        return new InitialJson();
                    case VOWEL:
                        return new VowelJson();
                    case FINAL:
                        return new FinalJson();
                    case NUMBER:
                        return new NumberJson();
                    case ALPHABET:
                        return new AlphabetJson();
                    case SENTENCE:
                        return new SentenceJson();
                    case ABBREVIATION:
                        return new AbbreviationJson();
                    default:
                        return null;
                }
            case MASTER_PRACTCE:
                switch(MiddleMenuNumber){
                    case LETTER:
                        return new LetterJson();
                    case WORD:
                        return new WordJson();
                    default:
                        return null;
                }
            case QUIZ:
                switch (MiddleMenuNumber){
                    case INITIAL_QUIZ:
                        return new InitialJson();
                    case VOWEL_QUIZ:
                        return new VowelJson();
                    case FINAL_QUIZ:
                        return new FinalJson();
                    case NUMBER_QUIZ:
                        return new NumberJson();
                    case ALPHABET_QUIZ:
                        return new AlphabetJson();
                    case SENTENCE_QUIZ:
                        return new SentenceJson();
                    case ABBREVIATION_QUIZ:
                        return new AbbreviationJson();
                    case LETTER_QUIZ:
                        return new WordJson();
                    case WORD_QUIZ:
                        return new LetterJson();
                    default:
                        return null;
                }
            default:
                return null;
        }
    }

    public int getMenuNumber(Deque<Integer> MenuAdreeDeque){
        if(!MenuAdreeDeque.isEmpty())
            return MenuAdreeDeque.removeFirst();
        else
            return -1;
    }
}

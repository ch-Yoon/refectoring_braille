package com.project.why.braillelearning.BrailleInformationFactory;

import com.project.why.braillelearning.Practice.Constant;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;

/**
 * Created by hyuck on 2017-09-13.
 */

public class BrailleFactory implements BrailleInformationFactory {
    /* 메뉴 구성
     * tutorial
     * basicpractice (initial, vowel, final, number, alphabet, sentence, abbreviation)
     * masterpractce (letter, word)
     * translation
     * quiz (initial_quiz, vowel_quiz, final_quiz, number_quiz, alphabet_quiz, sentence_quiz, abbreviation_quiz, letter_quiz, word_quiz)
     * mynote (basic_note, master_note, communication_note)
     * communication (teacher_mode, student_mode)
     */

    @Override
    public Getting getInformationObject(String menuName) {
        switch(menuName){
            case Constant.TUTORIAL:
                return new Tutorial();
            case Constant.INITIAL_BASIC:
                return new Initial();
            case Constant.VOWEL_BASIC:
                return new Vowel();
            case Constant.FINAL_BASIC:
                return new Final();
            case Constant.NUMBER_BASIC:
                return new Number();
            case Constant.ALPHABET_BASIC:
                return new Alphabet();
            case Constant.SENTENCE_BASIC:
                return new Sentence();
            case Constant.ABBREVIATION_BASIC:
                return new Abbreviation();
            case Constant.LETTER_MASTER:
                return new Letter();
            case Constant.WORD_MASTER:
                return new Word();
            case Constant.TRANSLATION:
                return new Translation();
            case Constant.INITIAL_READINGQUIZ:
                return new InitialReadingQuiz();
            case Constant.INITIAL_WRITINGQUIZ:
                return new InitialWritingQuiz();
            case Constant.VOWEL_READINGQUIZ:
                return new VowelReadingQuiz();
            case Constant.VOWEL_WRITINGQUIZ:
                return new VowelWritingQuiz();
            case Constant.FINAL_READINGQUIZ:
                return new FinalReadingQuiz();
            case Constant.FINAL_WRITINGQUIZ:
                return new FinalWritingQuiz();
            case Constant.NUMBER_READINGQUIZ:
                return new NumberReadingQuiz();
            case Constant.NUMBER_WRITINGQUIZ:
                return new NumberWritingQuiz();
            case Constant.ALPHABET_READINGQUIZ:
                return new AlphabetReadingQuiz();
            case Constant.ALPHABET_WRITINGQUIZ:
                return new AlphabetWritingQuiz();
            case Constant.SENTENCE_READINGQUIZ:
                return new SentenceReadingQuiz();
            case Constant.SENTENCE_WRITINGQUIZ:
                return new SentenceWritingQuiz();
            case Constant.ABBREVIATION_READINGQUIZ:
                return new AbbreviationReadingQuiz();
            case Constant.ABBREVIATION_WRITINGQUIZ:
                return new AbbreviationWritingQuiz();
            case Constant.WORD_READINGQUIZ:
                return new WordReadingQuiz();
            case Constant.WORD_WRITINGQUIZ:
                return new WordWritingQuiz();
            case Constant.BASIC_MYNOTE:
                return new BasicNote();
            case Constant.MASTER_MYNOTE:
                return new MasterNote();
            case Constant.COMMUNICATION_MYNOTE:
                return new CommunicationNote();
            case Constant.TEACHER_COMMUNICATION:
                return new Teacher();
            case Constant.STUDENT_COMMUNICATION:
                return new Student();
            default:
                return null;
        }
    }
}

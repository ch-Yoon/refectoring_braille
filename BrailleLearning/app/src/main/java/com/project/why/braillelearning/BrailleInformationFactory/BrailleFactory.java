package com.project.why.braillelearning.BrailleInformationFactory;

import com.project.why.braillelearning.Practice.Constant;

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
    public Getting getInformationObject(Deque<Integer> Deque) {
        Deque<Integer> MenuAdreeDeque = new LinkedList<>();
        MenuAdreeDeque.addAll(Deque);
        int BigMenuNumber = getMenuNumber(MenuAdreeDeque);
        int MiddleMenuNumber = getMenuNumber(MenuAdreeDeque);
        int SmallMenuNumber = getMenuNumber(MenuAdreeDeque);

        switch(BigMenuNumber){
            case Constant.TUTORIAL:
                return new Tutorial();
            case Constant.BASIC:
                switch(MiddleMenuNumber){
                    case Constant.BASIC_INITIAL:
                        return new Initial();
                    case Constant.BASIC_VOWEL:
                        return new Vowel();
                    case Constant.BASIC_FINAL:
                        return new Final();
                    case Constant.BASIC_NUMBER:
                        return new Number();
                    case Constant.BASIC_ALPHABET:
                        return new Alphabet();
                    case Constant.BASIC_SENTENCE:
                        return new Sentence();
                    case Constant.BASIC_ABBREVIATION:
                        return new Abbreviation();
                    default:
                        return null;
                }
            case Constant.MASTER:
                switch(MiddleMenuNumber){
                    case Constant.MASTER_LETTER :
                        return new Letter();
                    case Constant.MASTER_WORD :
                        return new Word();
                    default:
                        return null;
                }
            case Constant.TRANSLATION:
                return new Translation();
            case Constant.QUIZ:
                switch (MiddleMenuNumber){
                    case Constant.QUIZ_INITIAL:
                        switch(SmallMenuNumber){
                            case Constant.READING_QUIZ :
                                return new InitialReadingQuiz();
                            case Constant.WRITING_QUIZ :
                                return new InitialWritingQuiz();
                            default :
                                return null;
                        }
                    case Constant.QUIZ_VOWEL:
                        switch(SmallMenuNumber){
                            case Constant.READING_QUIZ :
                                return new VowelReadingQuiz();
                            case Constant.WRITING_QUIZ :
                                return new VowelWritingQuiz();
                            default :
                                return null;
                        }
                    case Constant.QUIZ_FINAL:
                        switch(SmallMenuNumber){
                            case Constant.READING_QUIZ :
                                return new FinalReadingQuiz();
                            case Constant.WRITING_QUIZ :
                                return new FinalWritingQuiz();
                            default :
                                return null;
                        }
                    case Constant.QUIZ_NUMBER :
                        switch(SmallMenuNumber){
                            case Constant.READING_QUIZ :
                                return new NumberReadingQuiz();
                            case Constant.WRITING_QUIZ :
                                return new NumberWritingQuiz();
                            default :
                                return null;
                        }
                    case Constant.QUIZ_ALPHABET :
                        switch(SmallMenuNumber){
                            case Constant.READING_QUIZ :
                                return new AlphabetReadingQuiz();
                            case Constant.WRITING_QUIZ :
                                return new AlphabetWritingQuiz();
                            default :
                                return null;
                        }
                    case Constant.QUIZ_SENTENCE :
                        switch(SmallMenuNumber){
                            case Constant.READING_QUIZ :
                                return new SentenceReadingQuiz();
                            case Constant.WRITING_QUIZ :
                                return new SentenceWritingQuiz();
                            default :
                                return null;
                        }
                    case Constant.QUIZ_ABBREVIATION :
                        switch(SmallMenuNumber){
                            case Constant.READING_QUIZ :
                                return new AbbreviationReadingQuiz();
                            case Constant.WRITING_QUIZ :
                                return new AbbreviationWritingQuiz();
                            default :
                                return null;
                        }
                    case Constant.QUIZ_LETTER :
                        switch(SmallMenuNumber){
                            case Constant.READING_QUIZ :
                                return new LetterReadingQuiz();
                            case Constant.WRITING_QUIZ :
                                return new LetterWritingQuiz();
                            default :
                                return null;
                        }
                    case Constant.QUIZ_WORD :
                        switch(SmallMenuNumber){
                            case Constant.READING_QUIZ :
                                return new WordReadingQuiz();
                            case Constant.WRITING_QUIZ :
                                return new WordWritingQuiz();
                            default :
                                return null;
                        }
                    default:
                        return null;
                }
            case Constant.MYNOTE:
                switch(MiddleMenuNumber){
                    case Constant.MYNOTE_BASIC :
                        return new BasicNote();
                    case Constant.MYNOTE_MASTER :
                        return new MasterNote();
                    case Constant.MYNOTE_COMMUNICATION :
                        return new CommunicationNote();
                }
            case Constant.COMMUNICATION :
                switch (MiddleMenuNumber) {
                    case Constant.COMMUNICATION_TEACHER :
                        return new Teacher();
                    case Constant.COMMUNICATION_STUDENT :
                        return new Student();
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

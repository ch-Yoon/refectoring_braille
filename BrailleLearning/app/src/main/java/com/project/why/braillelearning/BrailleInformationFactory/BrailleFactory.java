package com.project.why.braillelearning.BrailleInformationFactory;

import com.project.why.braillelearning.EnumConstant.Menu;

/**
 * Created by hyuck on 2017-09-13.
 */


/**
 * 점자 정보 factory class
 *
 * 메뉴 구성
 * tutorial : x
 * basicpractice : initial, vowel, final, number, alphabet, sentence, abbreviation
 * masterpractce : letter, word
 * translation : x
 * quiz : initial_quiz, vowel_quiz, final_quiz, number_quiz, alphabet_quiz, sentence_quiz, abbreviation_quiz, letter_quiz, word_quiz
 * mynote : basic_note, master_note, communication_note
 * communication : teacher_mode, student_mode
 */
public class BrailleFactory implements BrailleInformationFactory {
    @Override
    public GettingInformation getInformationObject(Menu menuName) {
        switch(menuName){
            case TUTORIAL:
                return new Tutorial();
            case INITIAL_BASIC:
                return new Initial();
            case VOWEL_BASIC:
                return new Vowel();
            case FINAL_BASIC:
                return new Final();
            case NUMBER_BASIC:
                return new Number();
            case ALPHABET_BASIC:
                return new Alphabet();
            case SENTENCE_BASIC:
                return new Sentence();
            case ABBREVIATION_BASIC:
                return new Abbreviation();
            case LETTER_MASTER:
                return new Letter();
            case WORD_MASTER:
                return new Word();
            case TRANSLATION:
                return new Translation();
            case INITIAL_QUIZ:
                return new InitialQuiz();
            case VOWEL_QUIZ:
                return new VowelQuiz();
            case FINAL_QUIZ:
                return new FinalQuiz();
            case NUMBER_QUIZ:
                return new NumberQuiz();
            case ALPHABET_QUIZ:
                return new AlphabetQuiz();
            case SENTENCE_QUIZ:
                return new SentenceQuiz();
            case ABBREVIATION_QUIZ:
                return new AbbreviationQuiz();
            case LETTER_QUIZ:
                return new LetterQuiz();
            case WORD_QUIZ:
                return new WordQuiz();
            case BASIC_MYNOTE:
                return new BasicNote();
            case MASTER_MYNOTE:
                return new MasterNote();
            case COMMUNICATION_MYNOTE:
                return new CommunicationNote();
            case TEACHER_COMMUNICATION:
                return new Teacher();
            case STUDENT_COMMUNICATION:
                return new Student();
            default:
                return null;
        }
    }
}

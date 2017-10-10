package com.project.why.braillelearning.BrailleInformationFactory;

import com.project.why.braillelearning.EnumConstant.Menu;

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
            case INITIAL_READINGQUIZ:
                return new InitialReadingQuiz();
            case INITIAL_WRITINGQUIZ:
                return new InitialWritingQuiz();
            case VOWEL_READINGQUIZ:
                return new VowelReadingQuiz();
            case VOWEL_WRITINGQUIZ:
                return new VowelWritingQuiz();
            case FINAL_READINGQUIZ:
                return new FinalReadingQuiz();
            case FINAL_WRITINGQUIZ:
                return new FinalWritingQuiz();
            case NUMBER_READINGQUIZ:
                return new NumberReadingQuiz();
            case NUMBER_WRITINGQUIZ:
                return new NumberWritingQuiz();
            case ALPHABET_READINGQUIZ:
                return new AlphabetReadingQuiz();
            case ALPHABET_WRITINGQUIZ:
                return new AlphabetWritingQuiz();
            case SENTENCE_READINGQUIZ:
                return new SentenceReadingQuiz();
            case SENTENCE_WRITINGQUIZ:
                return new SentenceWritingQuiz();
            case ABBREVIATION_READINGQUIZ:
                return new AbbreviationReadingQuiz();
            case ABBREVIATION_WRITINGQUIZ:
                return new AbbreviationWritingQuiz();
            case WORD_READINGQUIZ:
                return new WordReadingQuiz();
            case WORD_WRITINGQUIZ:
                return new WordWritingQuiz();
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

package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class WordleDictionaryHelperTest {
    private WordleDictionary dictionary;

    @BeforeEach
    void beforeEach() {
        dictionary = new WordleDictionary();
    }

    @Test
    void correctnessChekShouldReturnOKForValidWOrd() {
        dictionary.addWords(List.of("аврал", "поезд"));

        WordleDictionaryHelper helper = new WordleDictionaryHelper(dictionary);

        try {
            assertTrue(helper.correctnessChek("аврал", 5));
        } catch (WordCorrectnessException e) {
            fail("Не должно быть исключения: " + e.getMessage());
        }
    }

    @Test
    void correctnessChekShouldReturnWrongLength() {
        dictionary.addWords(List.of("аврал", "поезд"));

        WordleDictionaryHelper helper = new WordleDictionaryHelper(dictionary);

        try {
            assertFalse(helper.correctnessChek("наврал", 5));
            fail("Должно быть выброшено исключение WordCorrectnessException");;
        } catch (WordCorrectnessException e) {
            assertEquals("Длина слова должна быть равна 5.", e.getMessage());
        }
    }

    @Test
    void correctnessChekShouldReturnNotInDictionary() {
        dictionary.addWords(List.of("аврал", "поезд"));

        WordleDictionaryHelper helper = new WordleDictionaryHelper(dictionary);

        try {
            helper.correctnessChek("абзац", 5);
            fail("Должно быть выброшено исключение WordCorrectnessException");
        } catch (WordCorrectnessException e) {
            assertEquals("Слово абзац не содержится в словаре.", e.getMessage());
        }
    }

    @Test
    void hasAnyLettersShouldReturnTrueForLettersFromWord() {
        assertTrue(WordleDictionaryHelper.hasAnyLetters("аврал", Set.of('а', 'р')));
        assertTrue(WordleDictionaryHelper.hasAnyLetters("поезд", Set.of('о', 'е')));
    }

    @Test
    void hasAnyLettersShouldReturnFalseForLettersNotFromWord() {
        assertFalse(WordleDictionaryHelper.hasAnyLetters("аврал", Set.of('г', 'м')));
        assertFalse(WordleDictionaryHelper.hasAnyLetters("поезд", Set.of('г', 'м')));
    }

    @Test
    void hasAnyLettersShouldReturnTrueForLettersNotFromWordAndFromWord() {
        assertTrue(WordleDictionaryHelper.hasAnyLetters("аврал", Set.of('л', 'м')));
        assertTrue(WordleDictionaryHelper.hasAnyLetters("поезд", Set.of('д', 'м')));
    }

    @Test
    void hasAllLettersShouldReturnTrueForLettersAllFromWord() {
        assertTrue(WordleDictionaryHelper.hasAllLetters("аврал", Set.of('а', 'в', 'р', 'л')));
        assertTrue(WordleDictionaryHelper.hasAllLetters("поезд", Set.of('п', 'о')));
    }

    @Test
    void hasAllLettersShouldReturnFalseForLettersAllNotFromWord() {
        assertFalse(WordleDictionaryHelper.hasAllLetters("аврал", Set.of('б', 'л')));
        assertFalse(WordleDictionaryHelper.hasAllLetters("поезд", Set.of('н', 'к')));
    }

    @Test
    void hasAllLettersShouldReturnFalseForLettersAnyNotFromWordAndAnyFromWord() {
        assertFalse(WordleDictionaryHelper.hasAllLetters("аврал", Set.of('б', 'в', 'р', 'л')));
        assertFalse(WordleDictionaryHelper.hasAllLetters("поезд", Set.of('п', 'е', 'о', 'л')));
    }

    @Test
    void hasLettersInSomePlaceShouldReturnTrueForRightPositionOfLetterFromWord() {
        assertTrue(WordleDictionaryHelper.hasLettersInSomePlace("аврал", 'а', 3));
        assertTrue(WordleDictionaryHelper.hasLettersInSomePlace("поезд", 'о', 1));
    }

    @Test
    void hasLettersInSomePlaceShouldReturnFalseForIncorrectPositionOfLetterFromWord() {
        assertFalse(WordleDictionaryHelper.hasLettersInSomePlace("аврал", 'а', 2));
        assertFalse(WordleDictionaryHelper.hasLettersInSomePlace("поезд", 'о', 0));
    }

    @Test
    void hasLettersInSomePlaceShouldReturnFalseIfGivenIndexBiggerThenWordLength() {
        assertFalse(WordleDictionaryHelper.hasLettersInSomePlace("аврал", 'а', 5));
        assertFalse(WordleDictionaryHelper.hasLettersInSomePlace("поезд", 'о', 6));
    }

}
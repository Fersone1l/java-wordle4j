package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordleDictionaryTest {
    private WordleDictionary dictionary;

    @BeforeEach
    void beforeEach() {
        PrintWriter logWriter = new PrintWriter(System.out, true);
        dictionary = new WordleDictionary();

    }

    @Test
    void addWordsShouldAllowContainsCheck() {
        dictionary.addWords(List.of("аврал", "поезд"));

        assertTrue(dictionary.contains("аврал"));
        assertTrue(dictionary.contains("поезд"));
        assertFalse(dictionary.contains("кобра"));
    }

    @Test
    void filterByLengthShouldReturnWOrdsOfGivenLength() {

        dictionary.addWords(List.of("аврал", "поезд", "обезьяна", "дом", "мак"));

        List<String> testList = dictionary.filterByLength(3);

        assertEquals(2, testList.size());

        assertFalse(testList.contains("аврал"));
        assertFalse(testList.contains("поезд"));
        assertFalse(testList.contains("обезьяна"));
        assertTrue(testList.contains("дом"));
        assertTrue(testList.contains("мак"));
    }

    @Test
    void emptyDictionaryShouldReturnEmptyFilterAndFalseForContains() {
        assertFalse(dictionary.contains("аврал"));
        assertTrue(dictionary.filterByLength(5).isEmpty());
    }

    @Test
    void dictionaryShouldHandleDuplicateWords() {
        dictionary.addWords(List.of("аврал", "аврал"));

        assertTrue(dictionary.contains("аврал"));
        assertEquals(2, dictionary.filterByLength(5).size()); // filterByLength возвращает все дубликаты
    }
}
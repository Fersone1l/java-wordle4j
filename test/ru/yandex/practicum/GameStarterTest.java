package ru.yandex.practicum;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameStarterTest {
    private WordleGame game;
    private WordleDictionary dictionary;
    private PrintWriter logWriter;

    @BeforeEach
    void beforeEach() {
        logWriter = new PrintWriter(System.out, true);
        game = new WordleGame();
        dictionary = new WordleDictionary();
    }

    @Test
    void gameStarterShouldFilterWordsByLengthAndSetDictionary() {
        dictionary.addWords(List.of("аврал", "поезд", "обезьяна", "дом", "мак"));
        int length = 5;

        new GameStarter(game, logWriter, dictionary, length);

        List<String> testList = dictionary.filterByLength(length);

        assertEquals(2, testList.size());

        assertTrue(testList.contains("аврал"));
        assertTrue(testList.contains("поезд"));
        assertFalse(testList.contains("обезьяна"));
        assertFalse(testList.contains("дом"));
        assertFalse(testList.contains("мак"));
    }

    @Test
    void gameStarterShouldThrowOnEmptyDictionary() {
        assertThrows(EmptyDictionaryException.class, () -> {
            new GameStarter(game, logWriter, dictionary, 5);
        });
    }

    @Test
    void gameStarterShouldSetAnswerFromFilteredDictionary() {
        dictionary.addWords(List.of("аврал", "поезд", "обезьяна", "дом", "мак"));
        int length = 5;

        GameStarter gameStarter = new GameStarter(game, logWriter, dictionary, length);

        String word = gameStarter.getRandomWord(dictionary);

        assertNotNull(word);
        assertTrue(dictionary.filterByLength(length).contains(word));
    }
}
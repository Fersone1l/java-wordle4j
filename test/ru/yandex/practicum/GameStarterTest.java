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
    private final int length = 5;

    @BeforeEach
    void beforeEach() {
        logWriter = new PrintWriter(System.out, true);
        game = new WordleGame();
        dictionary = new WordleDictionary();
    }

    @Test
    void gameStarterShouldFilterWordsByLengthAndSetDictionary() {
        dictionary.addWords(List.of("аврал", "поезд", "обезьяна", "дом", "мак"));

        GameStarter gameStarter = new GameStarter(logWriter, dictionary, length);

        gameStarter.initializeGame(game);

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
            GameStarter gameStarter = new GameStarter(logWriter, dictionary, length);
            gameStarter.initializeGame(game);
        });
    }

    @Test
    void gameStarterShouldSetAnswerFromFilteredDictionary() {
        dictionary.addWords(List.of("аврал", "поезд", "обезьяна", "дом", "мак"));

        GameStarter gameStarter = new GameStarter(logWriter, dictionary, length);
        gameStarter.initializeGame(game);

        String word = gameStarter.getRandomWord(dictionary);

        assertNotNull(word);
        assertTrue(dictionary.filterByLength(length).contains(word));
    }
}
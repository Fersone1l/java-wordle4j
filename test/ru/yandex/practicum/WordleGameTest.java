package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class WordleGameTest {

    private WordleGame game;

    @BeforeEach
    void beforeEach() {
        PrintWriter logWriter = new PrintWriter(System.out, true);
        game = new WordleGame();
        game.setLogWriter(logWriter);
    }

    @Test
    void evaluateShouldHandleRepeatedLettersCorrectly() {
        game.setAnswer("кокос");

        List<LetterResult> letterResults = game.evaluate("ооооо").results();

        assertEquals(LetterResult.ABSENT, letterResults.get(0));
        assertEquals(LetterResult.CORRECT, letterResults.get(1));
        assertEquals(LetterResult.ABSENT, letterResults.get(2));
        assertEquals(LetterResult.CORRECT, letterResults.get(3));
        assertEquals(LetterResult.ABSENT, letterResults.get(4));
    }

    @Test
    void evaluateShouldMixPresentAndAbsentWhenLettersRepeat() {
        game.setAnswer("кораб");

        List<LetterResult> letterResults = game.evaluate("роооо").results();

        assertEquals(LetterResult.PRESENT, letterResults.get(0));
        assertEquals(LetterResult.CORRECT, letterResults.get(1));
        assertEquals(LetterResult.ABSENT, letterResults.get(2));
        assertEquals(LetterResult.ABSENT, letterResults.get(3));
        assertEquals(LetterResult.ABSENT, letterResults.get(4));
    }

    @Test
    void evaluateSimpleTestOnAllCorrect() {
        game.setAnswer("аврал");

        List<LetterResult> letterResults = game.evaluate("аврал").results();

        assertEquals(LetterResult.CORRECT, letterResults.get(0));
        assertEquals(LetterResult.CORRECT, letterResults.get(1));
        assertEquals(LetterResult.CORRECT, letterResults.get(2));
        assertEquals(LetterResult.CORRECT, letterResults.get(3));
        assertEquals(LetterResult.CORRECT, letterResults.get(4));
    }

    @Test
    void evaluateSimpleTestOnAllAbsent() {
        game.setAnswer("поезд");

        List<LetterResult> letterResults = game.evaluate("экран").results();

        assertEquals(LetterResult.ABSENT, letterResults.get(0));
        assertEquals(LetterResult.ABSENT, letterResults.get(1));
        assertEquals(LetterResult.ABSENT, letterResults.get(2));
        assertEquals(LetterResult.ABSENT, letterResults.get(3));
        assertEquals(LetterResult.ABSENT, letterResults.get(4));
    }

    @Test
    void evaluateTwoDifferentWordsWithSameLetters() {
        game.setAnswer("атлас");

        List<LetterResult> letterResults = game.evaluate("салат").results();

        assertEquals(LetterResult.PRESENT, letterResults.get(0));
        assertEquals(LetterResult.PRESENT, letterResults.get(1));
        assertEquals(LetterResult.CORRECT, letterResults.get(2));
        assertEquals(LetterResult.CORRECT, letterResults.get(3));
        assertEquals(LetterResult.PRESENT, letterResults.get(4));
    }

    @Test
    void evaluateShouldHandleOverusedLettersCorrectly() {
        game.setAnswer("аллея");

        List<LetterResult> letterResults = game.evaluate("лаллл").results();

        assertEquals(LetterResult.PRESENT, letterResults.get(0));
        assertEquals(LetterResult.PRESENT, letterResults.get(1));
        assertEquals(LetterResult.CORRECT, letterResults.get(2));
        assertEquals(LetterResult.ABSENT, letterResults.get(3));
        assertEquals(LetterResult.ABSENT, letterResults.get(4));
    }

    @Test
    void updatingKnownLettersTestShouldHandlePresentLetters() {
        game.setAnswer("атлас");

        String userAnswer = "салат";
        UserResult userResult = game.evaluate(userAnswer);
        game.updatingKnownLetters(userResult ,userAnswer);

        Set<Character> required = game.getRequiredLetters();
        Set<Character> expected = Set.of('с', 'а', 'л', 'т');

        assertEquals(required, expected);

        assertEquals('с', game.getBannedPosition().get(0));
        assertEquals('а', game.getBannedPosition().get(1));
        assertEquals('т', game.getBannedPosition().get(4));

    }

    @Test
    void updatingKnownLettersShouldAddCorrectLetters() {
        game.setAnswer("аврал");

        String userAnswer = "авххх";
        UserResult userResult = game.evaluate(userAnswer);
        game.updatingKnownLetters(userResult ,userAnswer);

        assertTrue(game.getRequiredLetters().contains('а'));
        assertTrue(game.getRequiredLetters().contains('в'));

        assertEquals('а', game.getFixedPosition().get(0));
        assertEquals('в', game.getFixedPosition().get(1));
    }

    @Test
    void updatingKnownLettersShouldAddForbiddenLetters() {
        game.setAnswer("поезд");

        String userAnswer = "экран";
        UserResult userResult = game.evaluate(userAnswer);
        game.updatingKnownLetters(userResult ,userAnswer);

        Set<Character> forbidden = game.getForbiddenLetters();
        Set<Character> expected = Set.of('э', 'к', 'р', 'а', 'н');

        assertEquals(forbidden, expected);
    }

    @Test
    void updatingKnownLettersShouldNotKeepLetterInForbiddenIfItBecomesRequired() {
        game.setAnswer("щетка");

        String userAnswer = "каска";
        UserResult userResult = game.evaluate(userAnswer);
        game.updatingKnownLetters(userResult,userAnswer);

        Set<Character> forbidden = game.getForbiddenLetters();
        Set<Character> required = game.getRequiredLetters();
        Set<Character> expectedForbidden = Set.of('с');
        Set<Character> expectedRequired = Set.of('к', 'а');

        assertEquals(forbidden, expectedForbidden);
        assertEquals(required, expectedRequired);
    }

    @Test
    void updatingKnownLettersShouldAccumulateInformation() {
        game.setAnswer("кораб");

        String firstUserAnswer = "роооо";
        String secondUserAnswer = "караб";

        UserResult firstUserResult = game.evaluate(firstUserAnswer);
        game.updatingKnownLetters(firstUserResult, firstUserAnswer);

        UserResult secondUserResult = game.evaluate(secondUserAnswer);
        game.updatingKnownLetters(secondUserResult, secondUserAnswer);

        assertTrue(game.getRequiredLetters().contains('к'));
        assertTrue(game.getRequiredLetters().contains('о'));
        assertTrue(game.getRequiredLetters().contains('р'));
        assertTrue(game.getRequiredLetters().contains('а'));
        assertTrue(game.getRequiredLetters().contains('б'));
    }

    @Test
    void updatingKnownLettersShouldAddBannedPositionsCorrectly() {
        game.setAnswer("кораб");

        String userAnswer = "кобра";

        UserResult userResult = game.evaluate(userAnswer);
        game.updatingKnownLetters(userResult, userAnswer);

        assertEquals('б', game.getBannedPosition().get(2));
        assertEquals('р', game.getBannedPosition().get(3));
        assertEquals('а', game.getBannedPosition().get(4));
    }
}
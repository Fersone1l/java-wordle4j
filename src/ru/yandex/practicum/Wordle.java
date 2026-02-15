package ru.yandex.practicum;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Wordle {
    public static final String WORDS_FILE_PATH = "words_ru.txt";
    public static final String LOG_FILE_PATH = "log.txt";
    private static final Scanner scanner = new Scanner(System.in);
    private static final int wordsLength = 5;
    private static final int steps = 6;

    public static void main(String[] args) {

        try (FileOutputStream fos = new FileOutputStream(LOG_FILE_PATH);
             Writer writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
             PrintWriter logWriter = new PrintWriter(writer, true)) {

            logWriter.println("=== Игра Wordle запущена ===");

            WordleDictionaryLoader loader = new WordleDictionaryLoader(logWriter);
            WordleDictionary dictionary = loader.loadFromFIle(WORDS_FILE_PATH);

            WordleGame game = new WordleGame();

            GameStarter gameStarter = new GameStarter(logWriter, dictionary, wordsLength);
            gameStarter.initializeGame(game);

            WordleDictionaryHelper helper = new WordleDictionaryHelper(dictionary);

            System.out.println("Отгадайте слово из " + wordsLength + " букв за " + steps + " попыток!");
            playGame(game, helper);

            logWriter.println("=== Игра завершена ===");

        } catch (Exception e) {
            System.out.println("Произошла ошибка: " + e.getMessage());
            try (PrintWriter logWriter = new PrintWriter(new FileWriter(LOG_FILE_PATH, true))) {
                e.printStackTrace(logWriter);
            } catch (IOException ioEx) {
                System.out.println("Не удалось записать лог ошибки: " + ioEx.getMessage());
            }
        }
    }

    public static void playGame(WordleGame game, WordleDictionaryHelper helper) {
        while (game.getStep() != steps) {
            String userAnswer = checkAnswer(helper);

            if (userAnswer.isEmpty()) {
                userAnswer = game.getHint();
                System.out.println("Подсказка: " + userAnswer);
            }

            game.makeGuess(userAnswer);
            UserResult result = game.getUserResults().get(game.getStep() - 1);
            System.out.println(resultOfGuess(result));

            if (!result.results().contains(LetterResult.ABSENT) &&
                    !result.results().contains(LetterResult.PRESENT)) {
                System.out.println("Победа!");
                return;
            }
        }
        System.out.println("Игра окончена");
    }

    public static String checkAnswer(WordleDictionaryHelper helper) {
        String userAnswer = null;
        while (true) {
            userAnswer = scanner.nextLine();
            if (userAnswer.isEmpty()) {
                break; // пустой ввод — подсказка
            }
            try {
                if (helper.correctnessChek(userAnswer, wordsLength)) {
                    break;
                }
            } catch (WordCorrectnessException e) {
                System.out.println(e.getMessage());
            }
        }
        return userAnswer;
    }



    public static String resultOfGuess(UserResult userResult) {
        StringBuilder result = new StringBuilder();
        for (LetterResult letterResult : userResult.results()) {
            switch (letterResult) {
                case CORRECT:
                    result.append("+");
                    break;
                case PRESENT:
                    result.append("^");
                    break;
                case ABSENT:
                    result.append("-");
                    break;
            }
        }
        return result.toString();
    }
}

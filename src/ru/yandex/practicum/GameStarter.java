package ru.yandex.practicum;

import java.io.PrintWriter;
import java.util.List;
import java.util.Random;

public class GameStarter {

    PrintWriter logWriter;
    WordleDictionary dictionary;
    int wordsLength;

    public GameStarter(WordleGame game, PrintWriter logWriter, WordleDictionary dictionary, int wordsLength) {
        this.logWriter = logWriter;
        this.dictionary = dictionary;
        this.wordsLength = wordsLength;
        initializeGame(game);
    }

    private void initializeGame(WordleGame game) {
        game.setLogWriter(logWriter);
       List<String> wordsForGame = dictionary.filterByLength(wordsLength);
        if (wordsForGame.isEmpty()) {
            throw new EmptyDictionaryException("Игровой словарь пуст. Не найдено ни одного слова длиной в " + wordsLength + " символов");
        }
        logWriter.println("В игровой словарь добавлено " + wordsForGame.size() + " слов длиной " + wordsLength + " символов");

        game.setDictionary(wordsForGame);

        game.setAnswer(getRandomWord(dictionary));
        logWriter.println("Загадано слово <" + game.getAnswer() + ">.");
    }

    public String getRandomWord(WordleDictionary dictionary) {
        Random random = new Random();
        List<String> words = dictionary.filterByLength(wordsLength);

        return words.get(random.nextInt(words.size()));
    }
}

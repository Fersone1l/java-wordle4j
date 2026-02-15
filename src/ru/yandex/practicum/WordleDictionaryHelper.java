package ru.yandex.practicum;

import java.util.Set;

public class WordleDictionaryHelper {
    private final WordleDictionary dictionary;

    public WordleDictionaryHelper(WordleDictionary dictionary) {
        this.dictionary = dictionary;
    }

    public boolean correctnessChek(String word, int length) throws WordCorrectnessException {
        if (word.length() != length) {
            throw new WordCorrectnessException("Длина слова должна быть равна " + length + ".");
        } else if (!dictionary.contains(word)) {
            throw new WordCorrectnessException("Слово " + word + " не содержится в словаре.");
        }
        return true;
    }

    public static boolean hasAnyLetters(String word, Set<Character> characters) {
        for (char character : word.toCharArray()) {
            if (characters.contains(character)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasAllLetters(String word, Set<Character> characters) {
        for (Character character : characters) {
            if (!word.contains(String.valueOf(character))) {
                return false;
            }
        }
        return true;
    }


    public static boolean hasLettersInSomePlace(String word, char character, int index) {
        if (index >= word.length()) {
            return false;
        }
        return word.charAt(index) == character;
    }
}

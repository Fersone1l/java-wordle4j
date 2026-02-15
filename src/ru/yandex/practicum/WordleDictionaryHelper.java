package ru.yandex.practicum;

import java.util.Set;

public class WordleDictionaryHelper {
    WordleDictionary dictionary;

    public WordleDictionaryHelper(WordleDictionary dictionary) {
        this.dictionary = dictionary;
    }

    public WordCorrectness correctnessChek(String word, int length) {
        if (word.length() != length) {
            return WordCorrectness.WRONG_LENGTH;
        } else if (!dictionary.contains(word)) {
            return WordCorrectness.NOT_IN_DICTIONARY;
        }
        return WordCorrectness.OK;
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

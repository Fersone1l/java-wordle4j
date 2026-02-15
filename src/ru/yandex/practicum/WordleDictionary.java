package ru.yandex.practicum;

import java.util.*;

/*
этот класс содержит в себе список слов List<String>
    его методы похожи на методы списка, но учитывают особенности игры
    также этот класс может содержать рутинные функции по сравнению слов, букв и т.д.
 */
public class WordleDictionary {

    private final List<String> allWords = new ArrayList<>();
    private Set<String> allWordsSet = new HashSet<>();

    public void addWords(Collection<String> dictionary) {
        this.allWords.addAll(dictionary);
        this.allWordsSet = new HashSet<>(allWords);
    }

    public List<String> filterByLength(int length) {
        List<String> result = new ArrayList<>();
        for (String word : allWords) {
            if (word.length() == length) {
                result.add(word);
            }
        }
        return result;
    }

    public boolean contains(String word) {
        return allWordsSet.contains(word);
    }
}

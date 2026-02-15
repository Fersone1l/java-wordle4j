package ru.yandex.practicum;

public class DictionaryDoesNotContainsWord extends Exception {
    public DictionaryDoesNotContainsWord(String message) {
        super(message);
    }
}

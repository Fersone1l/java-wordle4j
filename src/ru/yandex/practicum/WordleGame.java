package ru.yandex.practicum;

import java.io.PrintWriter;
import java.util.*;

/*
в этом классе хранится словарь и состояние игры
    текущий шаг
    всё что пользователь вводил
    правильный ответ

в этом классе нужны методы, которые
    проанализируют совпадение слова с ответом
    предложат слово-подсказку с учётом всего, что вводил пользователь ранее

не забудьте про специальные типы исключений для игровых и неигровых ошибок
 */
public class WordleGame {

    private String answer;

    private int step = 0;

    private List<String> dictionary;

    private final List<UserResult> userResults = new ArrayList<>();

    private final Set<Character> requiredLetters = new HashSet<>();

    private final Set<Character> forbiddenLetters = new HashSet<>();

    private final Map<Integer, Character> fixedPosition = new HashMap<>();

    private final Map<Integer, Character> bannedPosition = new HashMap<>();

    private PrintWriter logWriter;

    private final Random random = new Random();

    public void setLogWriter(PrintWriter logWriter) {
        this.logWriter = logWriter;
    }

    public List<UserResult> getUserResults() {
        return userResults;
    }

    public void setDictionary(List<String> dictionary) {
        this.dictionary = dictionary;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public int getStep() {
        return step;
    }

    public void makeGuess(String userAnswer) {
        logWriter.println("В список ответов пользователя добавлено слово: " + userAnswer);

        step++;
        logWriter.println("Количество шагов стало равным: " + step);

        UserResult userResult = evaluate(userAnswer);
        userResults.add(userResult);
        updatingKnownLetters(userResult, userAnswer);
    }

    public String getHint() {
        List<String> dictionaryForHints = getDictionaryForHints();
        return dictionaryForHints.get(random.nextInt(dictionaryForHints.size()));
    }

    private List<String> getDictionaryForHints() {
        List<String> dictionaryForHints = new ArrayList<>();

        for (String word : dictionary) {
            if (WordleDictionaryHelper.hasAnyLetters(word, forbiddenLetters)) continue;

            if (!WordleDictionaryHelper.hasAllLetters(word, requiredLetters)) continue;

            boolean coincidence = true;
            for (Map.Entry<Integer, Character> entry : fixedPosition.entrySet()) {
                if (!WordleDictionaryHelper.hasLettersInSomePlace(word, entry.getValue(), entry.getKey())) {
                    coincidence = false;
                    break;
                }
            }

            if (!coincidence) continue;

            for (Map.Entry<Integer, Character> entry : bannedPosition.entrySet()) {
                if (WordleDictionaryHelper.hasLettersInSomePlace(word, entry.getValue(), entry.getKey())) {
                    coincidence = false;
                    break;
                }
            }

            if (coincidence) {
                dictionaryForHints.add(word);
            }
        }
        if (!dictionaryForHints.isEmpty()) {
            logWriter.println("Словарь для подсказок вычислен успешно. " + " В нем " + dictionaryForHints.size() + " слов.");
        } else {
            throw new EmptyDictionaryException("Ошибка вычисления словаря для подсказок.");
        }
        return dictionaryForHints;
    }

    public UserResult evaluate(String guess) {
        int length = answer.length();
        LetterResult[] result = new LetterResult[length];
        boolean[] used = new boolean[length];

        for (int i = 0; i < length; i++) {
            if (guess.charAt(i) == answer.charAt(i)) {
                result[i] = LetterResult.CORRECT;
                used[i] = true;
            }
        }

        for (int i = 0; i < length; i++) {
            if (result[i] != null) {
                continue;
            }

            char c = guess.charAt(i);
            result[i] = LetterResult.ABSENT;

            for (int j = 0; j < length; j++) {
                if (!used[j] && answer.charAt(j) == c) {
                    result[i] = LetterResult.PRESENT;
                    used[j] = true;
                    break;
                }
            }
        }
        logWriter.println("Проанализировано слово " + guess);
        return new UserResult(List.of(result));
    }

    public void updatingKnownLetters(UserResult userResult, String userAnswer) {
        int position = 0;
        for (LetterResult letterResult : userResult.results()) {
            char letter = userAnswer.charAt(position);
            if (letterResult == LetterResult.CORRECT) {
                fixedPosition.put(position, letter);
                requiredLetters.add(letter);
            } else if (letterResult == LetterResult.PRESENT) {
                requiredLetters.add(letter);
                bannedPosition.put(position, letter);
            } else {
                if (!requiredLetters.contains(letter)) {
                forbiddenLetters.add(letter);
                }
            }
            position++;
        }
        forbiddenLetters.removeAll(requiredLetters);
        logWriter.println("Обновлена информация о буквах.");
        logWriter.println("Required: " + requiredLetters);
        logWriter.println("Forbidden: " + forbiddenLetters);
        logWriter.println("Fixed: " + fixedPosition);
        logWriter.println("Banned: " + bannedPosition);
        logWriter.println();
    }

    public Set<Character> getRequiredLetters() {
        return requiredLetters;
    }

    public Set<Character> getForbiddenLetters() {
        return forbiddenLetters;
    }

    public Map<Integer, Character> getFixedPosition() {
        return fixedPosition;
    }

    public Map<Integer, Character> getBannedPosition() {
        return bannedPosition;
    }
}

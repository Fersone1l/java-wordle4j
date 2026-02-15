package ru.yandex.practicum;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/*
этот класс содержит в себе всю рутину по работе с файлами словарей и с кодировками
    ему нужны методы по загрузке списка слов из файла по имени файла
    на выходе должен быть класс WordleDictionary
 */
public class WordleDictionaryLoader {

    private final PrintWriter logWriter;

    public WordleDictionaryLoader(PrintWriter logWriter) {
        this.logWriter = logWriter;
    }

    public WordleDictionary loadFromFIle(String fileName) throws IOException {
        logWriter.println("Начало чтения файла со словарем.");
        WordleDictionary dictionary = new WordleDictionary();
        File wordsFile = getFile(fileName);
        List<String> words = readFile(wordsFile);
        dictionary.addWords(words);

        return dictionary;
    }

    public File getFile(String fileName) throws FileNotFoundException {
        logWriter.println("Ищем файл с именем " + fileName);
        Path path = Paths.get(fileName);
        File file = path.toFile();

        if (file.exists()) {
            logWriter.println("Файл " + fileName + " найден.");
            return file;
        } else {
            throw new FileNotFoundException(String.format("Файла с именем %s не существует.", fileName));
        }
    }

    public List<String> readFile(File file) throws IOException {
        List<String> fileInfo = new ArrayList<>();
        logWriter.println("Читаем файл " + file.getName());

        try (FileReader fileReader = new FileReader(file, StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            while (bufferedReader.ready()) {
                String word = bufferedReader.readLine();
                fileInfo.add(word);
            }
            if (!fileInfo.isEmpty()) {
                logWriter.println("Слова из файла " + file.getName() + " получены.");
                logWriter.println("Получено " + fileInfo.size() + " слов.");
            } else {
                logWriter.println("Слова из файла " + file.getName() + " не были получены.");
            }
        } catch (IOException e) {
            logWriter.println("Произошла ошибка чтения файла " + file.getName());
            throw e;
        }
        return fileInfo;
    }
}

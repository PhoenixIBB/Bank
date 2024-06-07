package org.example;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ImportManager {

    final String RESOURCES;
    String fileName;
    File[] files;
    List<String> fileNamesCache;

    public ImportManager(String RESOURCES) {
        this.RESOURCES = RESOURCES;
    }

    public void showDirectory() {

        File directory = new File(RESOURCES);
        try {
            if (directory.exists() && directory.isDirectory()) {

                File[] files = directory.listFiles();
                fileNamesCache = new ArrayList<>();
                fileNamesCache.add("Нулевой объект.");
                System.out.println("\nСодержимое текущей директории \"" + RESOURCES + "\": \n");
                    for (File file : files) {
                        int index = Arrays.asList(files).indexOf(file);
                        fileNamesCache.add(file.getName());
                        System.out.println((index + 1) + ". " + file.getName());
                    }
            } else System.out.println("Директория не существует.");
        } catch (NullPointerException e) {
            System.out.println("Ошибка импорта. Директория не считана или пуста.");
        }
    }

    public String chooseTheFile() {

        try {
            int i = 0;
            System.out.println();
            Scanner scan = new Scanner(System.in);
            int number = scan.nextInt();

            fileName = fileNamesCache.get(number);

        } catch (NullPointerException e) {
            System.out.println("Ошибка импорта. Возможно, в директории отсутствуют файлы или их форматы недопустимы.");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Ошибка импорта. Отсутствует файл с заданным индексом.");
        }
        return fileName;
    }

    public List<Transaction> collectInformation() throws IOException {
        Parsers parsers = null;
        String format;
        try {
            do {
                System.out.println("Выберите файл в директории. (введите его номер из списка)");
                showDirectory();
                String[] fileNameParts = chooseTheFile().split("\\.");
                System.out.println("\nИмя выбранного файла: " + fileNameParts[0] + "\nФормат выбранного файла: " + fileNameParts[1] + "\nСборка транзакций, пожалуйста, подождите...");
                format = fileNameParts[1];

                switch (format) {
                    case "xml" -> parsers = new ParserXML();
                    case "html" -> parsers = new ParserHTML();
                    case "csv" -> parsers = new ParserCSV();
                    case "json" -> parsers = new ParserJSON();
                    case "pdf" -> parsers = new ParserPDF();
                    default -> System.out.println("\nНеподдерживаемый формат!\n");
                }
            } while (parsers == null);

            if (format.equals("xml") || format.equals("html") || format.equals("csv") || format.equals("json")) {
                final Path path = Paths.get(RESOURCES + fileName);
                final List<String> lines = Files.readAllLines(path);
                System.out.println("Сборка и сортировка транзакций...");
                System.out.println("Строк собрано: " + lines.size() + ".");
                return parsers.collectValidatedTransactions(lines);

            } else {
                File file = new File(RESOURCES + fileName);

                try (PDDocument document = Loader.loadPDF(file)) {
                    PDFTextStripper pdfStripper = new PDFTextStripper();
                    String inputText = pdfStripper.getText(document);
                    String[] linesArray = inputText.split("\n");
                    List<String> lines = new ArrayList<>(Arrays.asList(linesArray));
                    return parsers.collectValidatedTransactions(lines);
                } catch (IOException e) {
                    System.out.println("Ошибка импорта. Не удалось загрузить PDF-файл.");;
                }
            }
        } catch (Exception e) {
            System.out.println("Ошибка импорта.");
        }
            return null;
        }
    }

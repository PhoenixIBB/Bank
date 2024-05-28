package org.example;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class ImportManager {

    final String RESOURCES;
    String fileName;

    public ImportManager (String RESOURCES) { this.RESOURCES = RESOURCES; }

    public void showDirectory () {
        File directory = new File(RESOURCES);
        int i = 0;
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            System.out.println("\nСодержимое текущей директории " + RESOURCES + ": \n");
            assert files != null;
            for (File file : files) {
                i++;
                System.out.println("№" + i + " " + file.getName());
            }
            chooseTheFile(files);
        } else System.out.println("Директория не существует.");
    }

    public void chooseTheFile (File[] files) {
        System.out.println("Выберите файл в директории. (введите его номер из списка)");
        int i = 0;
        Scanner scan = new Scanner(System.in);
        int number = scan.nextInt();

        for (File file : files) {
            i++;
            if (i == number) {
                fileName = file.getName();
            }
        }
    }

    public List<BankTransaction> collectInformation (final BankStatementParser bankStatementParser) throws IOException {
        showDirectory();
        System.out.println("\nВведите имя файла: \n");
//        Scanner scan = new Scanner(System.in);
//        String fileName = scan.nextLine();
        final Path path = Paths.get(RESOURCES + fileName);
        final List<String> lines = Files.readAllLines(path);
        System.out.println("Сборка и сортировка транзакций...");
        System.out.println("Строк собрано: " + lines.size());
        return bankStatementParser.collectValidatedTransactions(lines);
    }
}

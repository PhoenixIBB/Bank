package org.example;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ImportManager {

    final String RESOURCES;

    public ImportManager (String RESOURCES) { this.RESOURCES = RESOURCES; }

    public void showDirectory () {
        File directory = new File(RESOURCES);
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            System.out.println("\nСодержимое текущей директории " + RESOURCES + ": \n");
            assert files != null;
            for (File file : files) {
                System.out.println(file.getName());
            }
        } else System.out.println("Директория не существует.");
    }

    public List<BankTransaction> collectInformation (final BankStatementParser bankStatementParser) throws IOException {
        showDirectory();
        System.out.println("\nВведите имя файла: \n");
//        Scanner scan = new Scanner(System.in);
//        String fileName = scan.nextLine();
        final Path path = Paths.get(RESOURCES + "XMLfile.xml");
        final List<String> lines = Files.readAllLines(path);
        System.out.println("Сборка и сортировка транзакций...");
        System.out.println("Строк собрано: " + lines.size());
        return bankStatementParser.collectValidatedTransactions(lines);
    }
}

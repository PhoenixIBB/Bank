package Utility;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class LinesCounter {
    String RESOURCES = "src/main/resources/";
    public void collectInformation () throws IOException {
        final Path path = Paths.get(RESOURCES + "fileName.csv");
        final List<String> lines = Files.readAllLines(path);
        int count = 0;

        for (String line : lines) {
            System.out.println(line);
            count++;
        }
        System.out.println(count);
    }

    public static void main (String... args) throws IOException {
        LinesCounter lines = new LinesCounter();
        lines.collectInformation();
    }
}

import org.example.TransactorParserCSV;
import org.example.TransactorParsers;
import org.example.Transaction;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.example.TransactorParsers.DATE_PATTERN;

public class TransactorTransactorParserCSVTest {
    private final TransactorParsers statementParser = new TransactorParserCSV();

    @Test
    public void shouldParseAllCorrectLines() throws Exception {
        List<String> lines = new ArrayList<>();
        lines.add("30-01-2024,-450,Qesco");
        lines.add("30-01-2024,-1300,Tesco");
        lines.add("30-01-2024,-710,Mesco");

        final List<Transaction> result = statementParser.parseLinesFrom(lines);
        List<Transaction> expected = new ArrayList<>();
        expected.add(new Transaction(LocalDate.parse("30-01-2024", DATE_PATTERN), -450, "Qesco", true, null));
        expected.add(new Transaction(LocalDate.parse("30-01-2024", DATE_PATTERN), -1300, "Tesco", true, null));
        expected.add(new Transaction(LocalDate.parse("30-01-2024", DATE_PATTERN), -710, "Mesco", true, null));
        final double tolerance = 0.0d;

        Assert.assertEquals(expected.getFirst(), result.getFirst());
        Assert.assertEquals(expected, result);
    }

    // Добавить тесты с другими аргументами и без аргументов

//    @Test
//    public void shouldParseCorrectWithoutOneAttribute() throws Exception {
//        List<String> lines = new ArrayList<>();
//        lines.add("30-01-2024,,Qesco");
//        lines.add("30-01-2024,-1300,Pesco");
//        lines.add("30-01-2024,-710,Mesco");
//
//        final List<BankTransaction> result = statementParser.parseLinesFrom(lines);
//        List<BankTransaction> expected = new ArrayList<>();
//        expected.add(new BankTransaction(LocalDate.parse("30-01-2024", DATE_PATTERN), -450, "Qesco", true, null));
//        expected.add(new BankTransaction(LocalDate.parse("30-01-2024", DATE_PATTERN), -1300, "Tesco", true, null));
//        expected.add(new BankTransaction(LocalDate.parse("30-01-2024", DATE_PATTERN), -710, "Mesco", true, null));
//        final double tolerance = 0.0d;
//
//        Assert.assertEquals(expected.getFirst(), result.getFirst());
//        Assert.assertEquals(expected, result);
//    }
}

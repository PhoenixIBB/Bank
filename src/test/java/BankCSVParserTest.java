import org.example.BankCSVParser;
import org.example.BankStatementParser;
import org.example.BankTransaction;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;

public class BankCSVParserTest {
    private final BankStatementParser statementParser = new BankCSVParser();

    @Test
    public void shouldParseOneCorrectLIne() throws Exception {
        final String line = "30-01-2024,-300,Tesco";

        final BankTransaction result = statementParser.formatFrom(line);
        final BankTransaction expected = new BankTransaction(LocalDate.of(2024, Month.JANUARY, 30), -300, "Tesco", true, result.getNotification());
        final double tolerance = 0.0d;

        Assert.assertEquals(expected.getDate(), result.getDate());
        Assert.assertEquals(expected.getDescription(), result.getDescription());
        Assert.assertEquals(expected.getAmount(), result.getAmount(), tolerance);
    }

    // Добавить тесты с другими аргументами и без аргументов

    @Test
    public void shouldParseOneCorrectWithoutOneAttribute() throws Exception {
        final String line = "30-01-2024,-300";

        final BankTransaction result = statementParser.formatFrom(line);
        final BankTransaction expected = new BankTransaction(LocalDate.of(2024, Month.JANUARY, 30), -300, null, true, result.getNotification());
        final double tolerance = 0.0d;

        Assert.assertEquals(expected.getDate(), result.getDate());
        Assert.assertEquals(expected.getDescription(), result.getDescription());
        Assert.assertEquals(expected.getAmount(), result.getAmount(), tolerance);
    }

}

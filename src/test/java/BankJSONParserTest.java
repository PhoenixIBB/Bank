import org.example.BankJSONParser;
import org.example.BankStatementParser;
import org.example.BankTransaction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BankJSONParserTest {
    private final BankStatementParser statementParser = new BankJSONParser();
    List<String> lines;


    @Before
    public void setUp () {
        lines = new ArrayList<>();
    }

    @Test
    public void shouldParseAndCreateACorrectObjectsFromOneLine () {

        lines.add("[");
        lines.add("{\"date\": \"01-01-2023\", \"amount\": -250, \"category\": \"Страховка\"},");
        lines.add("{\"date\": \"08-01-2023\", \"amount\": -240, \"category\": \"Коммунальные услуги\"},");
        lines.add("{\"date\": \"11-01-2023\", \"amount\": -130, \"category\": \"Продукты\"},");
        lines.add("]");

        List<BankTransaction> result = statementParser.parseLinesFrom(lines);
        List<BankTransaction> expected = new ArrayList<>();
        expected.add(new BankTransaction(LocalDate.parse("01-01-2023", BankStatementParser.DATE_PATTERN), -250, "Страховка", true, null));
        expected.add(new BankTransaction(LocalDate.parse("08-01-2023", BankStatementParser.DATE_PATTERN), -240, "Коммунальные услуги", true, null));
        expected.add(new BankTransaction(LocalDate.parse("11-01-2023", BankStatementParser.DATE_PATTERN), -130, "Продукты", true, null));

        Assert.assertEquals(expected.get(0), result.get(0));
        Assert.assertEquals(expected.get(1), result.get(1));
        Assert.assertEquals(expected.get(2), result.get(2));
    }

    @Test
    public void shouldParseAndCreateACorrectObjectsFromDifferentLines () {

        lines.add("[");
        lines.add("{");
        lines.add("\t\"date\": \"06-01-2023\",");
        lines.add("\t\"amount\": -290, ");
        lines.add("\t\"category\": \"Одежда\"");
        lines.add("}");
        lines.add("]");

        List<BankTransaction> result = statementParser.parseLinesFrom(lines);
        List<BankTransaction> expected = new ArrayList<>();
        expected.add(new BankTransaction(LocalDate.parse("06-01-2023", BankStatementParser.DATE_PATTERN), -290, "Одежда", true, null));

        Assert.assertEquals(expected.get(0), result.get(0));
    }
}

import org.example.BankStatementProcessor;
import org.example.BankTransaction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BankStatementProcessorTest {
    List<BankTransaction> bankTransactions;
    private static final DateTimeFormatter DATE_PATTERN = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    @Before
    public void setUp() {
        bankTransactions = new ArrayList<>();
        bankTransactions.add(new BankTransaction(LocalDate.parse("12-02-2024", DATE_PATTERN),-2500,"Продукты", true, null));
        bankTransactions.add(new BankTransaction(LocalDate.parse("11-02-2024", DATE_PATTERN),-4500,"Электроника", true, null));
        bankTransactions.add(new BankTransaction(LocalDate.parse("05-03-2024", DATE_PATTERN),-500,"Кофе", true, null));
        bankTransactions.add(new BankTransaction(LocalDate.parse("16-04-2024", DATE_PATTERN),-1500,"Транспорт", true, null));
        bankTransactions.add(new BankTransaction(LocalDate.parse("02-05-2024", DATE_PATTERN),-1500,"Уход за собой", true, null));
        bankTransactions.add(new BankTransaction(LocalDate.parse("27-05-2024", DATE_PATTERN),-13500,"Одежда true", true, null));
    }

    //С пустой коллекцией
    @Test
    public void shouldReturnZeroForEmptyTransactionList() {
        bankTransactions.clear();

        BankStatementProcessor bankStatementProcessorTest = new BankStatementProcessor(bankTransactions);
        final double result = bankStatementProcessorTest.calculateTotalAmount();

        Assert.assertEquals(0.0, result, 0.0);
    }

    //С коллекцией с одной транзакцией
    @Test
    public void shouldSumWithOnlyOneValue() {

        //Инициализируем калькулятор
        bankTransactions.clear();
        bankTransactions.add(new BankTransaction(LocalDate.parse("27-05-2024", DATE_PATTERN),-13500,"Одежда",  true, null));
        BankStatementProcessor bankStatementProcessorTest = new BankStatementProcessor(bankTransactions);

        //Выполняем действие
        final double result = bankStatementProcessorTest.calculateTotalAmount();
        final double expected = -13500;

        //Проверяем
        Assert.assertEquals(expected, result, 0.0);
    }

    //С дубликатами данных
    @Test
    public void shouldSumWithSameCategory() {

        //Инициализируем калькулятор
        bankTransactions.clear();
        bankTransactions.add(new BankTransaction(LocalDate.parse("27-05-2024", DATE_PATTERN),-13500,"Одежда", true, null));
        bankTransactions.add(new BankTransaction(LocalDate.parse("13-05-2024", DATE_PATTERN),-13500,"Одежда", true, null));
        bankTransactions.add(new BankTransaction(LocalDate.parse("27-05-2024", DATE_PATTERN),-3500,"Одежда", true, null));
        BankStatementProcessor bankStatementProcessorTest = new BankStatementProcessor(bankTransactions);

        //Выполняем действие
        final double result = bankStatementProcessorTest.calculateTotalAmount();
        double expected = 0;
        for (BankTransaction bankTransaction : bankTransactions) {
            expected += bankTransaction.getAmount();
        }

        //Проверяем
        Assert.assertEquals(expected, result, 0.0);
    }

    //С полными дубликатами данных
    @Test
    public void shouldSumWithDuplicates() {

        //Инициализируем калькулятор
        bankTransactions.clear();
        bankTransactions.add(new BankTransaction(LocalDate.parse("27-05-2024", DATE_PATTERN),-13500,"Одежда", true, null));
        bankTransactions.add(new BankTransaction(LocalDate.parse("27-05-2024", DATE_PATTERN),-13500,"Одежда", true, null));
        bankTransactions.add(new BankTransaction(LocalDate.parse("27-05-2024", DATE_PATTERN),-13500,"Одежда", true, null));
        BankStatementProcessor bankStatementProcessorTest = new BankStatementProcessor(bankTransactions);

        //Выполняем действие
        final double result = bankStatementProcessorTest.calculateTotalAmount();
        double expected = 0;
        for (BankTransaction bankTransaction : bankTransactions) {
            expected += bankTransaction.getAmount();
        }
                //Проверяем
        Assert.assertEquals(expected, result, 0.0);
    }

    //С пустым аргументом
    @Test
    public void shouldCalculateTotalAmountFromSameData () {

        //Инициализируем калькулятор
        BankStatementProcessor bankStatementProcessorTest = new BankStatementProcessor(bankTransactions);

        //Выполняем действие
        final double result = bankStatementProcessorTest.calculateTotalAmount();
        double expected = 0;
        for (final BankTransaction bankTransaction : bankTransactions) {
                expected += bankTransaction.getAmount();
        }
        final double tolerance = 0.0d;

        //Проверяем
        Assert.assertEquals(expected, result, tolerance);
    }

    //В аргументе только месяц
    @Test
    public void shouldCalculateTotalAmountFromMonth () {

        //Инициализируем калькулятор
        BankStatementProcessor bankStatementProcessorTest = new BankStatementProcessor(bankTransactions);
        Month month = Month.APRIL;

        //Выполняем действие
        final double result = bankStatementProcessorTest.calculateTotalAmount(month);
        double expected = 0;
        for (final BankTransaction bankTransaction : bankTransactions) {
            if (bankTransaction.getDate().getMonth() == month) {
                expected += bankTransaction.getAmount();
            }
        }
        final double tolerance = 0.0d;

        //Проверяем
        Assert.assertEquals(expected, result, tolerance);
    }

    //В аргументе категория
    @Test
    public void shouldCalculateTotalAmountFromCategory () {

        //Инициализируем калькулятор
        BankStatementProcessor bankStatementProcessorTest = new BankStatementProcessor(bankTransactions);
        String category = "Продукты";

        //Выполняем действие
        final double result = bankStatementProcessorTest.calculateTotalAmount(category);
        double expected = 0;
        for (final BankTransaction bankTransaction : bankTransactions) {
            if(bankTransaction.getDescription().equals(category)) {
                expected += bankTransaction.getAmount();
            }
        }
        final double tolerance = 0.0d;

        //Проверяем
        Assert.assertEquals(expected, result, tolerance);
    }

    //В аргументе месяц и категория
    @Test
    public void shouldCalculateTotalAmountFromMonthAndCategory () {

        //Инициализируем калькулятор
        BankStatementProcessor bankStatementProcessorTest = new BankStatementProcessor(bankTransactions);
        Month month = Month.AUGUST;
        String category = "Одежда";

        //Выполняем действие
        final double result = bankStatementProcessorTest.calculateTotalAmount(month, category);
        double expected = 0;
        for (final BankTransaction bankTransaction : bankTransactions) {
            if(bankTransaction.getDate().getMonth() == month && bankTransaction.getDescription().equals(category)) {
                expected += bankTransaction.getAmount();
            }
        }
        final double tolerance = 0.0d;

        //Проверяем
        Assert.assertEquals(expected, result, tolerance);
    }
    //В аргументе год и категория
    @Test
    public void shouldCalculateTotalAmountFromYearAndCategory () {

        //Инициализируем калькулятор
        BankStatementProcessor bankStatementProcessorTest = new BankStatementProcessor(bankTransactions);
        int year = 2024;
        String category = "Транспорт";

        //Выполняем действие
        final double result = bankStatementProcessorTest.calculateTotalAmount(year, category);
        double expected = 0;
        for (final BankTransaction bankTransaction : bankTransactions) {
            if (bankTransaction.getDate().getYear() == year && bankTransaction.getDescription().equals(category)) {
                expected += bankTransaction.getAmount();
            }
        }
        final double tolerance = 0.0d;

        //Проверяем
        Assert.assertEquals(expected, result, tolerance);
    }
}

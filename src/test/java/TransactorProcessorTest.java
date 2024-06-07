import org.example.TransactorStatementProcessor;
import org.example.Transaction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TransactorProcessorTest {
    List<Transaction> transactions;
    private static final DateTimeFormatter DATE_PATTERN = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    @Before
    public void setUp() {
        transactions = new ArrayList<>();
        transactions.add(new Transaction(LocalDate.parse("12-02-2024", DATE_PATTERN),-2500,"Продукты", true, null));
        transactions.add(new Transaction(LocalDate.parse("11-02-2024", DATE_PATTERN),-4500,"Электроника", true, null));
        transactions.add(new Transaction(LocalDate.parse("05-03-2024", DATE_PATTERN),-500,"Кофе", true, null));
        transactions.add(new Transaction(LocalDate.parse("16-04-2024", DATE_PATTERN),-1500,"Транспорт", true, null));
        transactions.add(new Transaction(LocalDate.parse("02-05-2024", DATE_PATTERN),-1500,"Уход за собой", true, null));
        transactions.add(new Transaction(LocalDate.parse("27-05-2024", DATE_PATTERN),-13500,"Одежда true", true, null));
    }

    //С пустой коллекцией
    @Test
    public void shouldReturnZeroForEmptyTransactionList() {
        transactions.clear();

        TransactorStatementProcessor transactorStatementProcessorTest = new TransactorStatementProcessor(transactions);
        final double result = transactorStatementProcessorTest.calculateTotalAmount();

        Assert.assertEquals(0.0, result, 0.0);
    }

    //С коллекцией с одной транзакцией
    @Test
    public void shouldSumWithOnlyOneValue() {

        //Инициализируем калькулятор
        transactions.clear();
        transactions.add(new Transaction(LocalDate.parse("27-05-2024", DATE_PATTERN),-13500,"Одежда",  true, null));
        TransactorStatementProcessor transactorStatementProcessorTest = new TransactorStatementProcessor(transactions);

        //Выполняем действие
        final double result = transactorStatementProcessorTest.calculateTotalAmount();
        final double expected = -13500;

        //Проверяем
        Assert.assertEquals(expected, result, 0.0);
    }

    //С дубликатами данных
    @Test
    public void shouldSumWithSameCategory() {

        //Инициализируем калькулятор
        transactions.clear();
        transactions.add(new Transaction(LocalDate.parse("27-05-2024", DATE_PATTERN),-13500,"Одежда", true, null));
        transactions.add(new Transaction(LocalDate.parse("13-05-2024", DATE_PATTERN),-13500,"Одежда", true, null));
        transactions.add(new Transaction(LocalDate.parse("27-05-2024", DATE_PATTERN),-3500,"Одежда", true, null));
        TransactorStatementProcessor transactorStatementProcessorTest = new TransactorStatementProcessor(transactions);

        //Выполняем действие
        final double result = transactorStatementProcessorTest.calculateTotalAmount();
        double expected = 0;
        for (Transaction transaction : transactions) {
            expected += transaction.getAmount();
        }

        //Проверяем
        Assert.assertEquals(expected, result, 0.0);
    }

    //С полными дубликатами данных
    @Test
    public void shouldSumWithDuplicates() {

        //Инициализируем калькулятор
        transactions.clear();
        transactions.add(new Transaction(LocalDate.parse("27-05-2024", DATE_PATTERN),-13500,"Одежда", true, null));
        transactions.add(new Transaction(LocalDate.parse("27-05-2024", DATE_PATTERN),-13500,"Одежда", true, null));
        transactions.add(new Transaction(LocalDate.parse("27-05-2024", DATE_PATTERN),-13500,"Одежда", true, null));
        TransactorStatementProcessor transactorStatementProcessorTest = new TransactorStatementProcessor(transactions);

        //Выполняем действие
        final double result = transactorStatementProcessorTest.calculateTotalAmount();
        double expected = 0;
        for (Transaction transaction : transactions) {
            expected += transaction.getAmount();
        }
                //Проверяем
        Assert.assertEquals(expected, result, 0.0);
    }

    //С пустым аргументом
    @Test
    public void shouldCalculateTotalAmountFromSameData () {

        //Инициализируем калькулятор
        TransactorStatementProcessor transactorStatementProcessorTest = new TransactorStatementProcessor(transactions);

        //Выполняем действие
        final double result = transactorStatementProcessorTest.calculateTotalAmount();
        double expected = 0;
        for (final Transaction transaction : transactions) {
                expected += transaction.getAmount();
        }
        final double tolerance = 0.0d;

        //Проверяем
        Assert.assertEquals(expected, result, tolerance);
    }

    //В аргументе только месяц
    @Test
    public void shouldCalculateTotalAmountFromMonth () {

        //Инициализируем калькулятор
        TransactorStatementProcessor transactorStatementProcessorTest = new TransactorStatementProcessor(transactions);
        Month month = Month.APRIL;

        //Выполняем действие
        final double result = transactorStatementProcessorTest.calculateTotalAmount(month);
        double expected = 0;
        for (final Transaction transaction : transactions) {
            if (transaction.getDate().getMonth() == month) {
                expected += transaction.getAmount();
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
        TransactorStatementProcessor transactorStatementProcessorTest = new TransactorStatementProcessor(transactions);
        String category = "Продукты";

        //Выполняем действие
        final double result = transactorStatementProcessorTest.calculateTotalAmount(category);
        double expected = 0;
        for (final Transaction transaction : transactions) {
            if(transaction.getDescription().equals(category)) {
                expected += transaction.getAmount();
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
        TransactorStatementProcessor transactorStatementProcessorTest = new TransactorStatementProcessor(transactions);
        Month month = Month.AUGUST;
        String category = "Одежда";

        //Выполняем действие
        final double result = transactorStatementProcessorTest.calculateTotalAmount(month, category);
        double expected = 0;
        for (final Transaction transaction : transactions) {
            if(transaction.getDate().getMonth() == month && transaction.getDescription().equals(category)) {
                expected += transaction.getAmount();
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
        TransactorStatementProcessor transactorStatementProcessorTest = new TransactorStatementProcessor(transactions);
        int year = 2024;
        String category = "Транспорт";

        //Выполняем действие
        final double result = transactorStatementProcessorTest.calculateTotalAmount(year, category);
        double expected = 0;
        for (final Transaction transaction : transactions) {
            if (transaction.getDate().getYear() == year && transaction.getDescription().equals(category)) {
                expected += transaction.getAmount();
            }
        }
        final double tolerance = 0.0d;

        //Проверяем
        Assert.assertEquals(expected, result, tolerance);
    }
}

package org.example;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Random;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;


public class ExportManager {

    public void writeToWordFile(LocalDate startOfPeriod, LocalDate endOfPeriod, String userName, double roundedAmountExpense, double roundedAmountIncome, String mostExpensiveDescr1, String mostExpensiveDescr2, String mostExpensiveDescr3, String mostExpensiveDescr4, String mostExpensiveDescr5, double mostExpensive1, double mostExpensive2, double mostExpensive3, double mostExpensive4, double mostExpensive5, int redRandom, int greenRandom, int blueRandom) throws IOException, InvalidFormatException {

        XWPFDocument document = new XWPFDocument();

        XWPFParagraph firstPageParagraph = document.createParagraph();
        XWPFRun run = firstPageParagraph.createRun();

        String color = String.format("%02X%02X%02X", redRandom, greenRandom, blueRandom);
        run.setColor(color);
        run.setBold(true);
        run.setText("Отчет по выписке от ");
        run.setText(LocalDate.now().toString());

        run.addBreak();
        run.addBreak();

        run = firstPageParagraph.createRun();
        run.setColor(color);
        run.setBold(false);
        run.setText("Пользователя: ");
        run = firstPageParagraph.createRun();
        run.setColor(color);
        run.setBold(true);
        run.setText(userName);

        run.addBreak();
        run.addBreak();

        run = firstPageParagraph.createRun();
        run.setColor(color);
        run.setBold(false);
        run.setText("За период с: ");
        run = firstPageParagraph.createRun();
        run.setColor(color);
        run.setBold(true);
        run.setText(startOfPeriod.toString() + " — ");
        run.setBold(false);
        run.setText("по ");
        run.setBold(true);
        run.setText(endOfPeriod.toString());

        run.addBreak();
        run.addBreak();

        run = firstPageParagraph.createRun();
        run.setColor(color);
        run.setBold(false);
        run.setText("Всего затрат за период: ");
        run = firstPageParagraph.createRun();
        run.setColor(color);
        run.setBold(true);
        run.setText(String.valueOf(roundedAmountExpense));
        run.addBreak();
        run = firstPageParagraph.createRun();
        run.setColor(color);
        run.setBold(false);
        run.setText("Всего доход за период: ");
        run = firstPageParagraph.createRun();
        run.setColor(color);
        run.setBold(true);
        run.setText(String.valueOf(roundedAmountIncome));

        run.addBreak();
        run.addBreak();

        run = firstPageParagraph.createRun();
        run.setColor(color);
        run.setBold(false);
        run.setText("Топ-5 самых затратных категорий: ");
        run.addBreak();
        run = firstPageParagraph.createRun();
        run.setColor(color);
        run.setBold(true);
        run.setText("1. " + mostExpensiveDescr1 + ". Суммарно за период: " + mostExpensive1);
        run.addBreak();
        run.setText("2. " + mostExpensiveDescr2 + ". Суммарно за период: " + mostExpensive2);
        run.addBreak();
        run.setText("3. " + mostExpensiveDescr3 + ". Суммарно за период: " + mostExpensive3);
        run.addBreak();
        run.setText("4. " + mostExpensiveDescr4 + ". Суммарно за период: " + mostExpensive4);
        run.addBreak();
        run.setText("5. " + mostExpensiveDescr5 + ". Суммарно за период: " + mostExpensive5);

        run.addBreak();

        document.createParagraph().setPageBreak(true);

        XWPFParagraph secondPageParagraph = document.createParagraph();
        XWPFRun runDiagram = secondPageParagraph.createRun();

        runDiagram.setText("Диаграмма ваших расходов за период.");
        runDiagram.addBreak();

        String imagePath = "C:\\Users\\gilma\\Desktop\\BarChart.jpeg";
        FileInputStream chartInputStream = new FileInputStream(imagePath);
        runDiagram.addPicture(chartInputStream, XWPFDocument.PICTURE_TYPE_JPEG, imagePath, Units.toEMU(1200), Units.toEMU(500));
        chartInputStream.close();

        Random random = new Random(10);


        try (FileOutputStream out = new FileOutputStream("C:\\Users\\gilma\\Desktop\\Отчет по выписке за " + LocalDate.now() + ". №" + random.nextInt() + ".docx")) {
            document.write(out);
            System.out.println("Отчет успешно создан.");
        } catch (IOException e) {
            System.out.println("Ошибка экспорта. Не удалось экспортировать отчет.");
        }
    }
}

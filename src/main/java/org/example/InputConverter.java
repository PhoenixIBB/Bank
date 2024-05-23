package org.example;

import java.time.Month;
import java.util.InputMismatchException;
import java.util.Scanner;

public class InputConverter {

    public static double inputNumber () {
        Scanner scan = new Scanner(System.in);
        double input = -1;
        try {
            input = scan.nextDouble();
        } catch (IllegalArgumentException | InputMismatchException e) {
            System.out.println("\nОшибка конвертера! Неверный ввод числа!");
        }
            return input;
    }

    public static Month inputMonth () {
            Scanner scan = new Scanner(System.in);
            Month month = null;
            try {
                String mesyats = scan.nextLine();
                month = monthChooser(mesyats);
            } catch (IllegalArgumentException | InputMismatchException e) {
                System.out.println("\nОшибка конвертера! Неверный ввод месяца!");
            }
            return month;
    }

    // Перевод строчного представления в enum
    public static Month monthChooser (String mesyats) {
            return switch (mesyats) {
                case "Январь", "01", "январь", "January", "JANUARY", "Янв", "янв" -> Month.JANUARY;
                case "Февраль", "02", "февраль", "February", "FEBRUARY", "Фев", "фев" -> Month.FEBRUARY;
                case "Март", "03", "март", "March", "MARCH", "Мар", "мар" -> Month.MARCH;
                case "Апрель", "04", "апрель", "April", "APRIL", "Апр", "апр" -> Month.APRIL;
                case "Май", "05", "май", "May", "MAY" -> Month.MAY;
                case "Июнь", "06", "июнь", "June", "JUNE", "Июн", "июн" -> Month.JUNE;
                case "Июль", "07", "июль", "July", "JULY", "Июл", "июл" -> Month.JULY;
                case "Август", "08", "август", "August", "AUGUST", "Авг", "авг" -> Month.AUGUST;
                case "Сентябрь", "09", "сентябрь", "September", "SEPTEMBER", "Сен", "сен" -> Month.SEPTEMBER;
                case "Октябрь", "10", "октябрь", "October", "OCTOBER", "Окт", "окт" -> Month.OCTOBER;
                case "Ноябрь", "11", "ноябрь", "November", "NOVEMBER", "Ноя", "ноя" -> Month.NOVEMBER;
                case "Декабрь", "12", "декабрь", "December", "DECEMBER", "Дек", "дек" -> Month.DECEMBER;
                default -> throw new IllegalArgumentException("Ошибка конвертера. Недопустимый ввод месяца: " + mesyats);
            };
    }

    // Перевод enum в строчное представление
    public static String monthConverter (Month month) {
            String monthName;
            switch (month) {
                case Month.JANUARY -> monthName = "Январь";
                case Month.FEBRUARY -> monthName = "Февраль";
                case Month.MARCH -> monthName = "Март";
                case Month.APRIL -> monthName = "Апрель";
                case Month.MAY -> monthName = "Май";
                case Month.JUNE -> monthName = "Июнь";
                case Month.JULY -> monthName = "Июль";
                case Month.AUGUST -> monthName = "Август";
                case Month.SEPTEMBER -> monthName = "Сентябрь";
                case Month.OCTOBER -> monthName = "Октябрь";
                case Month.NOVEMBER -> monthName = "Ноябрь";
                case Month.DECEMBER -> monthName = "Декабрь";
                default -> throw new IllegalArgumentException("Ошибка конвертера. Некорретный ввод месяца.");
            }
            return monthName;
    }


}

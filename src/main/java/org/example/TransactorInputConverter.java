package org.example;

import java.time.Month;

public class TransactorInputConverter {

    public static String additionalDescriptionConverter(String additionalDescription) {

        return switch (additionalDescription) {
            // Супермаркеты
            case "00003", "EVO SVEZHEE MYASO", "FIKS PRAJS", "KARAVAN", "KARAVAN SEMEJNYJ",
                    "LENTA", "MAGAZIN", "MAGAZIN LUNA", "MAGNIT MM DARTFORD", "MAGNIT MM FETTI",
                    "MAGNIT MM KALEOS", "MONETKA", "MONETKA.", "MYASNAYA LAVKA", "OKEY",
                    "PEREKRESTOK VOYAZH", "PRODUKTY", "PYATEROCHKA" -> "Супермаркеты";

            // Интернет-магазины
            case "YM OZON" -> "Заказы";

            // Транспорт
            case "APP", "KUPIBILET.RU", "KupiBilet", "SP SPATO", "SPP", "TUTU.RU",
                    "V 433 TA", "WWW.IRAERO.RU", "YANDEX.TAXI", "YP KUPIBILET" -> "Транспорт";

            // Аптеки
            case "APTEKA", "APTEKA APREL", "APTEKA TSRA", "APTEKA ZEMLYANIKA", "OPTIMA-94" -> "Аптеки";

            // Банкоматы и банковские операции
            case "ATM", "KARTA-VKLAD", "MAPP_SBERBANK_ONL@IN_PAY", "MOBILE BANK: KOMISSIYA",
                    "Regular Charge", "SBERBANK ONL@IN KARTA-VKLAD", "SBERBANK ONL@IN VKLAD-KARTA",
                    "SBOL", "VKLAD-KARTA", "Альфа Банк", "ВТБ", "Тинькофф Банк" -> "Банковские операции";

            // Мобильная связь, интернет, телефония и коммунальные платежи
            case "PAY.MTS.RU TOPUP" -> "Мобильная связь, телефония";

            // Госуслуги, налоги, штрафы и т.п.
            case "GOSUSLUGI.RU", "NALOG.RU", "YM GOSUSLUGI"-> "Налоги и др.";

            // Программные продукты и подписки
            case "KASPERSKYLAB", "YM PLUS", "YM YANDEX.PLUS", "puzzle-english", "YM  guitarsolo" -> "Подписки";

            // Авто
            case "AVTOAKSESSUARY", "AVTOTEKHNIK", "URALSKIY REGION" -> "Автомобиль";

            // АЗС
            case "AZS 4-2", "AZS 7 OOO PROGRESS", "AZS NRG", "AZS YUGRA RU", "GAZPROMNEFT AZS", "AZS", "RNAZSBN02-026" -> "АЗС";

            // ИПшки
            case "Avanpost IP Svistunov S.O", "IP GUCOL A", "IP MASLENNIKOVA EV", "IP ZOROGLYAN K.L." -> "ИП";

            // Рестораны, кафе, столовые
            case "BUFET ZAL NAKOPITELY", "BUFET ZAL REGISTRATS", "BURGER KING", "KAFE DRAJV",
                    "KFC AURA", "KFC GLOBUS", "KFC LENTA POBEDY", "RESTORAN SOYUZ",
                    "STOL-NGDY NSN", "STOLOVAYA", "STOLOVAYA TPU" -> "Рестораны, кафе";

            // Строймагазины, сантехника, электрика
            case "GARVIN", "SAMSTROY PLAST", "SANTEKHNIKA" -> "Строймагазины, сантехника";

            // Товары для дома
            case "KHOZTOVARY", "SET MAGAZINOV FORTUNA", "SET MAGAZINOV ROLLER" -> "Все для дома";

            // Алкомаркеты и бары
            case "KRASNOE BELOE", "SKAJ-BAR" -> "Бары и алкомаркеты";

            // Одежда и спорт инвентарь
            case "LC WAIKIKI" -> "Одежда";

            // Спорт
            case "SPORTMASTER" -> "Спорт";

            // Игровые покупки
            case "YM 4GAME" -> "Игры";

            // Понятия не имею, что это
            case "CH02023" -> "Нераспознанные транзакции";

            default -> additionalDescription;
        };
    }

    // Перевод строчного представления в enum
    public static Month monthChooser(String mesyats) {
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
    public static String monthConverter(Month month) {
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

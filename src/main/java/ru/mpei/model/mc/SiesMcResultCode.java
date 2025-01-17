package ru.mpei.model.mc;

/**
 * Расшифровка кода ответа
 */
public class SiesMcResultCode {

    public String getInfo(int code) {
        String description = switch (code) {
            case 0 -> "Успешное выполнение";
            case 100 -> "Успешное выполнение. Выданы не все записи. Для получения остальных значений необходимо повторить запрос с увеличенным значением `lastCommandId`";
            case 101 -> "Условно успешное выполнение. Ответ был загружен ранее";
            case -106 -> "Ошибка. Некорректная структура входных данных";
            case -110 -> "Ошибка. Не удалось определить SIES-узел по его идентификатору или адресу защищаемого устройства";
            case -316 -> "Ошибка. Передан неожидаемый ответ на команду";
            case -595 -> "Ошибка. Нарушена целостность переданного ответа";
            case -596 -> "Ошибка. Некоторые ответы от SIES-узлов не обработаны";
            case -999 -> "Ошибка. Внутренняя ошибка. Необходимо повторить попытку позднее";
            default -> "???";
        };

        return code + "(" + description + ")";
    }
}

package com.gladun.buscompany.exception;

public enum ServerErrorCode {

    UNEXPECTED_EXCEPTION("Oops, something went wrong"),
    LOGIN_BUSY("Пользователь с таким логином уже существует"),
    USER_NOT_FOUND("Неверный логин или такого пользователя не существует"),
    WRONG_PASSWORD("Неверный пароль"),
    WRONG_COOKIE("Ошибка, неверные cookie, попробуйте перезайти в систему"),
    ACCESS_ERROR("Отказано в доступе, недостаточно прав"),
    EDIT_PASSWORD_ERROR("Новый пароль должен отличаться от старого"),
    BUS_NOT_FOUND("Не удалось найти требуемую марку автобуса"),
    TRIP_NOT_FOUND("Не удалось найти запрашиваемый рейс"),
    INCORRECT_SCHEDULE_PATTERN("Расписание рейсов указано неверно"),
    ILLEGAL_DATE_FORMAT("Введён неверный формат дат"),
    TRIP_DATE_NOT_FOUND("Не удалось найти запрашиваемую дату для данного рейса"),
    PASSENGER_NOT_FOUND("В запросе пассажир указан неверно или такого пассажира не существует"),
    PLACE_NOT_FOUND("Нет подходящего свободного места");


    private final String message;

    ServerErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

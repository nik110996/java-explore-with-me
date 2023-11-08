package ru.practicum.ewm.main.service.util;

import java.time.format.DateTimeFormatter;

public class Constants {

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
}

package ru.practicum.ewm.stats.dto.util;

import java.time.format.DateTimeFormatter;

public class Constants {

    public static final String FORMAT_OF_DATES = "yyyy-MM-dd HH:mm:ss";

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(FORMAT_OF_DATES);

}

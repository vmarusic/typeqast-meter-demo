package com.typeqast.meter.utils;

import com.typeqast.meter.validation.exception.ValidationException;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.TemporalAccessor;

import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

public class LegacyConversionUtil {

    public static final String DEFAULT_UNIT_OF_MEASUREMENT = "UNIT";

    public static Long convertLegacyDateToMillis(String date) throws ValidationException {

        try {
            DateTimeFormatter parser = new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("MMM").toFormatter();
            TemporalAccessor accessor = parser.parse(date);
            return LocalDateTime.now()
                    .with(Month.from(accessor))
                    .with(lastDayOfMonth())
                    .with(LocalTime.MAX)
                    .toInstant(ZoneOffset.UTC)
                    .toEpochMilli();
        } catch (Exception ex) {
            throw new ValidationException("Invalid date encountered");
        }

    }
    public static String convertMillisToLegacyDate(Long millis) {
        return Instant.ofEpochMilli(millis)
                .atOffset(ZoneOffset.UTC)
                .toLocalDateTime().format(DateTimeFormatter.ofPattern("MMM")).toUpperCase();
    }

}

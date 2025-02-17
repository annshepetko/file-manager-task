package com.ann.test.impl.validator;

import com.ann.test.interfaces.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class DateValidator implements Validator<String> {

    private final static Logger logger = LoggerFactory.getLogger(DateValidator.class);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    public boolean isValid(String date) {
        try {
            LocalDate.parse(date, formatter);
            return true;
        } catch (DateTimeParseException e) {
            logger.error("Date format is invalid, so row will be skipped: {}", LocalDateTime.now());
            return false;
        }
    }
}
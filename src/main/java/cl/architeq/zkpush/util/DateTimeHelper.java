package cl.architeq.zkpush.util;

import com.google.common.base.Strings;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateTimeHelper {

    public static LocalDate toLocalDate(String date) {

        if(!Strings.isNullOrEmpty(date)) {
            return LocalDate.parse(date, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        }
        return null;
    }

}

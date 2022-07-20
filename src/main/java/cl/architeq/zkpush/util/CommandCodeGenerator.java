package cl.architeq.zkpush.util;

import com.google.common.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class CommandCodeGenerator {

    @Autowired
    private Cache<LocalDate, Integer> codeCache;

    public String generate() {

        LocalDate date = LocalDate.now();
        Integer seq = codeCache.getIfPresent(date);
        if(seq != null) {
            int number = seq+1;
            codeCache.put(date, Integer.valueOf(number));
            return DateTimeFormatter.ofPattern("ddMMyy").format(date)+number;
        } else {
            codeCache.put(date, Integer.valueOf(0));
            return DateTimeFormatter.ofPattern("ddMMyy").format(date)+"0";
        }
    }

}

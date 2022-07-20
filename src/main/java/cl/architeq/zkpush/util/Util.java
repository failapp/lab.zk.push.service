package cl.architeq.zkpush.util;

import lombok.extern.slf4j.Slf4j;

import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
public class Util {

    public static final DateTimeFormatter formatDateTimeISO = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    public static final DateTimeFormatter formatTimeISO = DateTimeFormatter.ofPattern("HHmmss");
    public static final DateTimeFormatter formatDateISO = DateTimeFormatter.ofPattern("yyyyMMdd");
    public static final DateTimeFormatter formatDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static boolean between(int i, int minValueInclusive, int maxValueInclusive) {
        if (i >= minValueInclusive && i <= maxValueInclusive)
            return true;
        else
            return false;
    }

    public static void sleep(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            //throw new IllegalStateException(e);
            log.error("[x] utils method sleep.. {}", e.getMessage());
        }
    }

    public static String formatUserId(String userId) {

        if (Optional.ofNullable( userId ).isPresent()) {
            String id = String.format("%016d", Integer.valueOf(userId.trim()));
            return id;
        } else {
            return "";
        }
    }

    public static String conversionEventCode(int verType) {

        String eventCode = "01010101";

        if (verType == 1) // finger verification (ZK UFACE 800) ..
            eventCode = "01010103";

        if (verType == 4)  // card verification (ZK UFACE 800) ..
            eventCode = "01010102";

        if (verType == 15) // face verification (ZK UFACE 800) ..
            eventCode = "01010103";

        return eventCode;
    }

    public static Integer conversionFunctionCode(int verStatus) {

        Integer functionCode = 255;

        if (verStatus == 0) // (ZK UFACE 800) ..
            functionCode = 10;

        if (verStatus == 1) // (ZK UFACE 800) ..
            functionCode = 30;

        return functionCode;
    }

}

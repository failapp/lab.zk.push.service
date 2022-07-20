package cl.architeq.zkpush;


import cl.architeq.zkpush.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
public class TimeZoneTest {

    @Test
    public void mockTest() {
        log.info("mock test ..");
        assertThat(true).isTrue();
    }

    //@Test
    public void timezoneTest() {
        ZoneId zoneId = ZoneId.systemDefault();
        //America/Santiago
        //Sat, 24 Jul 2021 05:43:29 GMT
        log.info("zoneId system default -> {}", zoneId.toString());
        assertThat(true).isTrue();
    }

    //@Test
    public void formatCommandFetchAttendanceDataTest() {

        StringBuilder builder = new StringBuilder();

        String from = LocalDate.now().minusDays(2L).format(Util.formatDate) + " 00:00:00";
        String to = LocalDate.now().format(Util.formatDate) + " 23:59:59";
        String cmd = "DATA QUERY ATTLOG StartTime=";

        builder.append(cmd);
        builder.append(from).append("\t");
        builder.append("EndTime=").append(to);

        log.info("[x] command: {}", builder);
        assertThat(true).isTrue();

    }


}

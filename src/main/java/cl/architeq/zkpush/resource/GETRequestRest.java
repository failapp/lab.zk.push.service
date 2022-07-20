package cl.architeq.zkpush.resource;

import cl.architeq.zkpush.impl.command.Command;
import com.google.common.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.TimeZone;

@Slf4j
@RestController
public class GETRequestRest {

    @Autowired
    private Cache<String, Command> commandCache;

    @ResponseBody
    @GetMapping(value = "/iclock/getrequest", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> onDeviceReconnect(@RequestParam("SN")String sn, HttpServletResponse response) {

        //log.info("[getrequest] ..snDevice {}", sn);

        StringBuilder builder = new StringBuilder();
        commandCache.asMap().values().forEach(command -> {

            if(command.getDeviceSN().equals(sn) && (command.getLastSend() == null || command.getNextSchedule().compareTo(LocalDateTime.now()) <= 0)) {

                builder.append(command.getCommandString()).append("\r\n");

                command.setSendingCount(command.getSendingCount()+1);
                command.setLastSend(LocalDateTime.now());
                //command.setNextSchedule(command.getLastSend().plus(3, ChronoUnit.MINUTES));
                command.setNextSchedule(command.getLastSend().plus(1, ChronoUnit.MINUTES));

                log.info("[x] ID {} : TYPE {} : CMD {}", command.getCode(), command.getClass().getSimpleName(), command.getCommandString());
            }
        });

        if(builder.toString().length() == 0) {
            builder.append("OK");
        }
        return new ResponseEntity<>(builder.toString(), HttpStatus.OK);

    }

    /*
    @GetMapping("/response-entity-builder-with-http-headers")
    public ResponseEntity<String> usingResponseEntityBuilderAndHttpHeaders() {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Example-Header",
                "Value-ResponseEntityBuilderWithHttpHeaders");

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body("Response with header using ResponseEntity");
    }
    */

}

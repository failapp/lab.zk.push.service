package cl.architeq.zkpush;

import cl.architeq.zkpush.impl.command.Command;
import cl.architeq.zkpush.model.Device;
import cl.architeq.zkpush.repository.DeviceRepository;
import cl.architeq.zkpush.util.Util;
import com.google.common.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
public class CacheServiceTest {

    @Autowired
    private Cache<String, Command> commandCache;

    //@Autowired
    //private DeviceRepository deviceRepository;

    //@Test
    public void mockTest() {
        log.info("mock test ..");
        assertThat(true).isTrue();
    }

    //@Test
    public void cacheTest() {


        //Optional<Device> device = deviceRepository.findBySerialNumber(serial);

        commandCache.asMap().values().forEach(cmd -> {
            log.info("cache command ->  sn:{}, gen:{},  last send:{}, next schd:{}",
                    cmd.getDeviceSN(), cmd.getCode(), cmd.getLastSend(), cmd.getNextSchedule());
        });

        String code="2508211";
        Command command = commandCache.getIfPresent(code);

        if (Optional.ofNullable(command).isPresent()) {
            //commandCache.invalidate(command.getCode());
            log.info("command: {}", command);
            commandCache.invalidate(command.getCode());
        }

        Util.sleep(1);
        commandCache.asMap().values().forEach(cmd -> {
            log.info("cache command ->  sn:{}, gen:{},  last send:{}, next schd:{}",
                    cmd.getDeviceSN(), cmd.getCode(), cmd.getLastSend(), cmd.getNextSchedule());
        });

        assertThat(true).isTrue();
    }


}

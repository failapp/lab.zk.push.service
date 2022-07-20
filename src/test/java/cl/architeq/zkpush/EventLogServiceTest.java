package cl.architeq.zkpush;

import cl.architeq.zkpush.repository.EventLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
public class EventLogServiceTest {

    @Autowired
    private EventLogRepository eventLogRepository;

    @Test
    public void mockTest() {
        log.info("mock test ..");
        assertThat(true).isTrue();
    }

    //@Test
    public void findCustomersTest() {

        List<String> customerList = eventLogRepository.searchDistinctCustomerIdAndFlagSync();

        customerList.forEach( x -> {
            log.info("[x] customerId: {}", x);
        });

        assertThat(true).isTrue();
    }

}

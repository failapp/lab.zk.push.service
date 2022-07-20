package cl.architeq.zkpush.task;

import cl.architeq.zkpush.model.EventLog;
import cl.architeq.zkpush.service.EventLogService;
import cl.architeq.zkpush.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class EventLogTask {

    @Autowired
    private EventLogService eventLogService;

    @Async
    @Scheduled(fixedDelay = 30000, initialDelay = 10000) // cada 30 segundos ..
    public void synchronizeEventLogWithCloudServer() {

        List<String> customerList = eventLogService.filterDistinctCustomerIdAndFlagSync();
        for (String customerId: customerList) {
            List<EventLog> dataList = eventLogService.findByCustomerIdAndFlagSync(false, customerId);
            if (dataList.size() > 0) {
                log.info("[x] customerId: {} eventlogs size: {}", customerId, dataList.size());
                eventLogService.sendEventLogToCloudServer(dataList, customerId);
            }
            Util.sleep(5);
        }
    }

}

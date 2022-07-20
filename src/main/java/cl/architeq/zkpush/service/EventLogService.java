package cl.architeq.zkpush.service;

import cl.architeq.zkpush.config.RESTConfig;
import cl.architeq.zkpush.model.Device;
import cl.architeq.zkpush.model.EventLog;
import cl.architeq.zkpush.repository.DeviceRepository;
import cl.architeq.zkpush.repository.EventLogRepository;
import cl.architeq.zkpush.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class EventLogService {

    @Autowired
    private EventLogRepository eventLogRepository;
    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private HttpHeaders httpHeaders;

    public void handlerAttendanceEvent(Device device, Optional<String> body) {

        if (body.isEmpty()) return;

        try {

            Arrays.asList( body.get().split("\\n") ).forEach( row -> {

                String[] cols = row.trim().split("\\t");

                TemporalAccessor accessor = Util.formatDateTime.parse(cols[1]);
                LocalDateTime localDateTime = LocalDateTime.from(accessor);
                String eventDateTime = localDateTime.format(Util.formatDateTimeISO);
                String eventDate = localDateTime.format(Util.formatDateISO);
                String eventTime = localDateTime.format(Util.formatTimeISO);
                String userId = cols[0];
                Integer verificationStatus = Integer.valueOf(cols[2]);
                Integer verificationType = Integer.valueOf(cols[3]);

                Optional<EventLog> _eventLog = eventLogRepository.findByEventDateTimeAndUserIdAndMacDevice(eventDateTime, userId, device.getMacAddr());
                if (_eventLog.isPresent()) return;

                EventLog eventLog = new EventLog(eventDate, eventTime, userId, device.getCustomerId(), device.getMacAddr());
                eventLog.setVerType(verificationType);
                eventLog.setVerStatus(verificationStatus);

                EventLog eventdata = eventLogRepository.save(eventLog);
                log.info("[x] save eventlog:{}", eventdata);

            });

        } catch (Exception ex) {
            log.error("[x] error: {}", ex.getMessage());
        }

    }


    public List<String> filterDistinctCustomerIdAndFlagSync() {
        return eventLogRepository.searchDistinctCustomerIdAndFlagSync();
    }

    public List<EventLog> findByCustomerIdAndFlagSync(boolean sync, String customerId) {
        int page = 0;
        int perPage = 100;
        Pageable pageable = PageRequest.of(page, perPage, Sort.by(Sort.Direction.DESC, "eventDateTime"));
        return eventLogRepository.findBySyncAndCustomerId(sync, customerId, pageable).toList();
    }


    public void sendEventLogToCloudServer(List<EventLog> dataList, String customerId) {

        String url = RESTConfig.WEBSERVICE_URL + "/evento";
        try {

            Pageable pageable = PageRequest.of(0, 10);
            Optional<Device> device = deviceRepository.findByCustomerId(customerId, pageable).toList()
                                                        .stream().findFirst();
            if (device.isEmpty()) return;

            String customerKey = device.get().getCustomerKey();
            Optional<JSONObject> payload = this.buildJsonObject(dataList, customerId, customerKey);
            if (payload.isEmpty()) return;
            log.info("[x] payload: {}", payload.get());

            dataList.forEach( x -> {
                x.setSync(true);
                eventLogRepository.save(x);
            });

            this.httpHeaders.set("customer_id", customerId);
            this.httpHeaders.set("customer_key", customerKey);
            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<String> request = new HttpEntity<>(payload.get().toString(), this.httpHeaders);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

            if (response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.CREATED) {
                //log.info("data -> {}", response.getBody());
                log.info("[x] synchronize eventdata with cloud server.. ok, registers: {}", dataList.size());
            } else {
                dataList.forEach( x -> {
                    x.setSync(false);
                    eventLogRepository.save(x);
                });
            }

        } catch (RestClientException e) {
            log.info("error rest client exception.. url: {}, message: {}", url, e.getMessage());
            dataList.forEach( x -> {
                x.setSync(false);
                eventLogRepository.save(x);
            });
        } catch (Exception ex) {
            log.info("[x] error: {}", ex.getMessage());
            dataList.forEach( x -> {
                x.setSync(false);
                eventLogRepository.save(x);
            });
        }

    }


    private Optional<JSONObject> buildJsonObject(List<EventLog> dataList, String customerId, String customerKey) {

        try {

            JSONObject json = new JSONObject();
            JSONArray jsonArray = new JSONArray();

            json.put("customer_id", Integer.valueOf(customerId));
            json.put("llave", customerKey );

            dataList.forEach( x -> {
                jsonArray.put( x.buildJsonObject() );
            });

            if (jsonArray.length() > 0) {
                json.put("body", jsonArray);
                return Optional.of(json);
            }

        } catch (Exception ex) {
            log.info("[x] error: {}", ex.getMessage());
        }
        return Optional.empty();

    }


}

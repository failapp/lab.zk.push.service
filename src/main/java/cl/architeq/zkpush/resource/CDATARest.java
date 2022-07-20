package cl.architeq.zkpush.resource;

import cl.architeq.zkpush.model.Device;
import cl.architeq.zkpush.service.DeviceService;
import cl.architeq.zkpush.service.EventLogService;
import com.google.common.cache.Cache;
import cl.architeq.zkpush.impl.config.DeviceOption;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Slf4j
@RestController
public class CDATARest {

    @Autowired
    private Cache<String, DeviceOption> deviceOptionCache;

    @Autowired
    private DeviceService deviceService;
    @Autowired
    private EventLogService eventLogService;


    @ResponseBody
    @GetMapping(value =  "/iclock/cdata", produces = MediaType.TEXT_PLAIN_VALUE)
    public String handshake(@RequestParam("SN") Optional<String> serial,
                            @RequestParam("options")Optional<String> options,
                            @RequestParam("pushver")Optional<String> pushVer,
                            @RequestParam("language")Optional<String> lang,
                            HttpServletRequest request){

        Optional<Device> device = this.validateDevice(serial.get());
        if (device.isEmpty()) return "";

        log.info("[api] Handshake event from device with ip {} and sn {}", request.getRemoteAddr(), serial.orElse("Empty"));
        DeviceOption option = getOption(serial.get());

        StringBuilder response = new StringBuilder();
        response.append("GET OPTION FROM : "+serial.get()+"\n\r");
        response.append("Stamp="+option.getStamp().orElse("")+"\n\r");
        response.append("OpStamp="+option.getOpStamp().orElse("")+"\n\r");
        response.append("ErrorDelay="+option.getErrorDelay().orElse("")+"\n\r");
        response.append("Delay="+option.getDelay().orElse("")+"\n\r");
        response.append("TransTimes="+option.getTransTimes().orElse("")+"\n\r");
        response.append("TransInterval="+option.getTransInterval().orElse("")+"\n\r");
        response.append("TransFlag="+option.getTransFlag().orElse("")+"\n\r");
        //response.append("TimeZone=-4"+"\n\r");
        //response.append("TimeZone="+ option.getTimezone().orElse("-4") +"\n\r");
        response.append("TimeZone="+ device.get().getTimezone() +"\n\r");
        response.append("Realtime="+option.getRealtime().orElse("")+"\n\r");
        response.append("Encrypt="+option.getEncrypt().orElse("")+"\n\r");

        deviceService.fetchAttendanceData(serial.get(), 2L);

        return response.toString();

    }


    private DeviceOption getOption(@NonNull String serial) {
        DeviceOption option = deviceOptionCache.getIfPresent(serial);
        if(option == null) {
            option = new DeviceOption(serial);
        }
        return option;
    }


    @ResponseBody
    @PostMapping(value="/iclock/cdata", produces = MediaType.TEXT_PLAIN_VALUE)
    public String receive(@RequestParam("SN")String serial,
                          @RequestParam("table")String table,
                          @RequestParam("Stamp")Optional<String> stamp,
                          @RequestBody Optional<String> body) {

        log.info("[api] Receiving request info from device {} with type {}", serial, table);

        if (body.isEmpty()) return "OK";
        Optional<Device> device = this.validateDevice(serial);
        if (device.isEmpty()) return "OK";

        if (table.equalsIgnoreCase("ATTLOG")) {
            //log.info("ATTLOG:\n\r {} - stamp: {}", body.get(), stamp.get());
            this.getOption(serial).setStamp( Optional.ofNullable(stamp.orElse("")) );
            this.eventLogService.handlerAttendanceEvent(device.get(), body);
        }

        if(table.equalsIgnoreCase("USERINFO")) {
            log.info("USERINFO:\n\r{}", body.get());
            //userInfoProcessor.execute(serial, body.get());
        }



        if(table.equalsIgnoreCase("FINGERTMP")) {
            log.info("FINGERTMP:\n\r{}", body);
            //fingerProcessor.execute(serial, body.orElse(""));
        }

        return "OK";
    }

    Optional<Device> validateDevice(String serial) {
        Optional<Device> device = deviceService.findBySerialNumber(serial);
        log.info("[x] validate authorized device.. serial number: {}", serial);
        if (device.isPresent()) {
            if (!device.get().getEnabled()) Optional.empty();
            if (Optional.ofNullable(device.get().getMacAddr()).isEmpty() || device.get().getMacAddr().isEmpty()) Optional.empty();
            if (Optional.ofNullable(device.get().getCustomerId()).isEmpty() || device.get().getCustomerId().isEmpty()) Optional.empty();
            if (Optional.ofNullable(device.get().getCustomerKey()).isEmpty() || device.get().getCustomerKey().isEmpty()) Optional.empty();
        } else {
            return Optional.empty();
        }
        return device;
    }


}

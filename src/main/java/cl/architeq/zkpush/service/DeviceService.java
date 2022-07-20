package cl.architeq.zkpush.service;

import cl.architeq.zkpush.impl.command.Command;
import cl.architeq.zkpush.impl.command.GenericCommand;
import cl.architeq.zkpush.impl.command.LOGCommand;
import cl.architeq.zkpush.model.Device;
import cl.architeq.zkpush.repository.DeviceRepository;
import cl.architeq.zkpush.util.CommandCodeGenerator;
import cl.architeq.zkpush.util.Util;
import com.google.common.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class DeviceService {

    @Autowired
    private Cache<String, Command> commandCache;

    @Autowired
    private CommandCodeGenerator codeGen;

    @Autowired
    private DeviceRepository deviceRepository;


    public Map<String, Object> fetchDevices(int page) {

        Map<String, Object> map = new HashMap<>();
        int perPage = 100;
        page = page-1;

        Pageable pageable = PageRequest.of(page, perPage, Sort.by("id"));
        Page<Device> data = deviceRepository.findByEnabled(true, pageable);
        List<Device> deviceList = data.toList();

        map.put("page", page+1);
        map.put("perPage", perPage);
        map.put("totalPages", data.getTotalPages());
        map.put("totalElements", data.getTotalElements());
        map.put("data", deviceList);
        return map;
    }


    public Optional<Device> storeDevice(Device device) {

        if (Optional.ofNullable(device).isEmpty()) return Optional.empty();

        Optional<Device> _device = deviceRepository.findBySerialNumber(device.getSerialNumber().strip());

        if (_device.isEmpty()) {

            Device newDevice = new Device(device.getCustomerId(), device.getCustomerKey(), device.getName(),
                    device.getSerialNumber(), device.getMacAddr(), device.getTimezone());

            newDevice.setDescription(device.getDescription());
            newDevice.setMode(device.getMode());
            newDevice.setIpAddr(device.getIpAddr());
            newDevice.setFunction(device.getFunction());

            return Optional.of( deviceRepository.save(newDevice) );
        } else {
            device.setId( _device.get().getId());
            device.setCreated( _device.get().getCreated());
            device.setUpdated(LocalDateTime.now().format(Util.formatDateTimeISO ));
            return Optional.of( deviceRepository.save(device) );
        }
    }


    public Optional<Device> findBySerialNumber(String serialNumber) {
        //log.info("[x] find device by serial number: {}", serialNumber);
        return deviceRepository.findBySerialNumber(serialNumber);
    }

    public Optional<Device> findByMacAddr(String macAddr) {
        //log.info("[x] find device by mac address: {}", macAddr);
        return deviceRepository.findByMacAddr(macAddr);
    }

    public void deleteDevice(Device device) {
        log.info("[x] delete device: {}", device);
        deviceRepository.delete(device);
    }


    public void operationLog(String serialDevice) {

        //LOGCommand command = new LOGCommand(serialDevice, codeGen.generate());
        //commandCache.put(command.getCommandString(), command);
        //String cmd = "DATA QUERY ATTLOG StartTime=2021-07-01 00:00:00   EndTime=2021-08-15 23:59:59"; // ok ..
        //String cmd = "DATA QUERY USERINFO PIN=929292"; // ok ..
        String cmd = "DATA QUERY USERINFO PIN=959595"; // ok ..
        GenericCommand command = new GenericCommand(serialDevice, codeGen.generate(), cmd);
        commandCache.put(command.getCode(), command);

    }

    public void fetchAttendanceData(String serialDevice, Long days) {

        StringBuilder builder = new StringBuilder();
        String from = LocalDate.now().minusDays(days).format(Util.formatDate) + " 00:00:00";
        String to = LocalDate.now().format(Util.formatDate) + " 23:59:59";
        String cmd = "DATA QUERY ATTLOG StartTime=";

        builder.append(cmd);
        builder.append(from).append("\t");
        builder.append("EndTime=").append(to);

        log.info("[x] command attendance data: {}", builder);

        GenericCommand command = new GenericCommand(serialDevice, codeGen.generate(), builder.toString());
        commandCache.put(command.getCode(), command);
    }

}

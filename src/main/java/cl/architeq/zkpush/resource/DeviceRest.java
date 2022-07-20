package cl.architeq.zkpush.resource;

import cl.architeq.zkpush.model.Device;
import cl.architeq.zkpush.service.DeviceService;
import com.google.common.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("api/v3/cloud/zk")
public class DeviceRest {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private Cache<String, Device> deviceCache;

    @GetMapping(path = "/devices")
    public ResponseEntity<Map<String, Object>> fetchDevices(@RequestParam(value = "page", required = false) Integer page) {
        if (Optional.ofNullable(page).isEmpty()) page=1;
        Map<String, Object> dataList = deviceService.fetchDevices(page);
        return new ResponseEntity<>(dataList, HttpStatus.OK);
    }

    @GetMapping(path = "/devices/{macAddr}")
    public ResponseEntity<?> fetchDevice(@PathVariable(value = "macAddr") String macAddr) {

        if (Optional.ofNullable(macAddr).isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);


        Optional<Device>device = deviceService.findByMacAddr(macAddr.trim().toUpperCase());

        /*
        Optional<Device> device = Optional.ofNullable(deviceCache.getIfPresent(macAddr));
        if (device.isPresent()) {
            log.info("[cache] get device macAddr -> {}, device: {}", macAddr, device.get());
            return new ResponseEntity<>(device.get(), HttpStatus.OK);
        } else {
            device = deviceService.findByMacAddr(macAddr.trim().toUpperCase());
        }
        */

        if (device.isEmpty()) {
            log.info("[api] get device macAddr -> {}", macAddr);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        deviceCache.put(macAddr, device.get());
        log.info("[api] get device macAddr -> {}, device: {}", macAddr, device.get());
        return new ResponseEntity<>(device.get(), HttpStatus.OK);
    }

    @PostMapping(path = "/devices")
    public ResponseEntity<?> saveDevice(@RequestBody Device device) {

        Optional<Device> _device = deviceService.storeDevice(device);
        if (_device.isPresent())
            return new ResponseEntity<>(_device.get(), HttpStatus.OK);

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(path = "/devices")
    public ResponseEntity<Boolean> deleteDevice(@RequestParam(value = "serialNumber", required = false) String serialNumber,
                                                @RequestParam(value = "macAddr", required = false) String macAddress) {

        if (Optional.ofNullable(serialNumber).isEmpty() && Optional.ofNullable(macAddress).isEmpty())
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);

        if (Optional.ofNullable(serialNumber).isPresent()) {
            Optional<Device> device = deviceService.findBySerialNumber(serialNumber);
            if (device.isPresent())
                deviceService.deleteDevice(device.get());
            else
                return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }

        if (Optional.ofNullable(macAddress).isPresent()) {
            Optional<Device> device = deviceService.findByMacAddr(macAddress);
            if (device.isPresent())
                deviceService.deleteDevice(device.get());
            else
                return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(true, HttpStatus.OK);
    }


}

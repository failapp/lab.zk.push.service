package cl.architeq.zkpush.model;

import cl.architeq.zkpush.util.Util;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@Table(name = "devices")
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String customerId;
    private String customerKey;
    private String name;
    private String description;
    private Boolean enabled;

    @Column(name = "serial_number", nullable = false, unique = true)
    private String serialNumber;

    @Column(name = "mac_addr", nullable = false, unique = true)
    private String macAddr;

    private String ipAddr;
    private String timezone;
    private String mode;
    private String function;
    private Integer checksumType;
    private String created;
    private String updated;

    public Device(String customerId, String customerKey, String name, String serialNumber, String macAddr, String timezone) {
        this.customerId = customerId;
        this.customerKey = customerKey;
        this.name = name;
        this.description = "";
        this.enabled = true;
        this.serialNumber = serialNumber;
        this.macAddr = macAddr;
        this.ipAddr = "";
        this.timezone = timezone;
        this.mode = "";
        this.function = "";
        this.checksumType = 2;
        this.created = LocalDateTime.now().format(Util.formatDateTimeISO);
        this.updated = LocalDateTime.now().format(Util.formatDateTimeISO);
    }


}

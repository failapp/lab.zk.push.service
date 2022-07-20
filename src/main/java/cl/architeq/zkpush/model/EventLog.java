package cl.architeq.zkpush.model;

import cl.architeq.zkpush.util.Util;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONObject;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@Table(name = "eventlogs")
public class EventLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String eventDateTime;
    private String eventDate;
    private String eventTime;
    private String userId;
    private String customerId;
    private String systemDateTime;
    private Integer verType;
    private Integer verStatus;
    private String checksum;
    private String macDevice;
    private Boolean sync;

    public EventLog(String eventDate, String eventTime, String userId, String customerId, String macDevice) {
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.eventDateTime = eventDate + eventTime;
        this.userId = userId;
        this.customerId = customerId;
        this.macDevice = macDevice;
        this.systemDateTime = LocalDateTime.now().format(Util.formatDateTimeISO);
        this.verType = 0; // verification type
        this.verStatus = 0; // verification status
        this.checksum = "";
        this.sync = false;
    }

    public JSONObject buildJsonObject() {

        String eventCode = Util.conversionEventCode( this.verType );
        Integer functionKey = Util.conversionFunctionCode( this.verStatus );

        JSONObject json = new JSONObject();
        json.put("eventdatetime", this.getEventDateTime());
        json.put("eventcode", eventCode);
        json.put("accessid", Util.formatUserId(this.getUserId()));
        json.put("macaddr", this.getMacDevice());
        json.put("functionkey", functionKey.toString());
        json.put("checksum", this.getChecksum());
        json.put("checksum_type", 2);

        return json;

    }

}

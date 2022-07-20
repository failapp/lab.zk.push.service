package cl.architeq.zkpush.impl.config;

import java.util.Optional;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class DeviceOption {

    @NonNull
    private String serial;

    @NonNull
    private Optional<String> stamp = Optional.ofNullable(null);

    @NonNull
    private Optional<String> opStamp = Optional.ofNullable(null);

    private Optional<String> errorDelay = Optional.ofNullable("30");

    private Optional<String> delay = Optional.ofNullable("30");

    private Optional<String> transTimes = Optional.ofNullable("06:00;09:00;12:00;15:00;18:00;21:00;24:00;03:00");

    private Optional<String> transInterval = Optional.ofNullable("60");

    //private Optional<String> transFlag = Optional.ofNullable("1111000000");
    private Optional<String> transFlag = Optional.ofNullable("1111111110");
    //private Optional<String> transFlag = Optional.ofNullable("1111111111");

    private Optional<String> timezone = Optional.ofNullable("-4");

    private Optional<String> realtime = Optional.ofNullable("1");

    private Optional<String> encrypt = Optional.ofNullable("0");


    DeviceOption() {
    }

    public DeviceOption(@NonNull String serial) {
        this.serial = serial;
    }

}

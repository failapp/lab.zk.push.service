package cl.architeq.zkpush.impl.command;

import com.google.common.base.MoreObjects;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Vector;

@Getter
@Setter
public abstract class Command implements Serializable {

    private static final long serialVersionUID = 1L;

    private Instant issued = Instant.now();

    @NonNull
    private String deviceSN;

    public abstract String getCommandString();

    private int sendingCount=0;

    @NonNull
    private String code;

    private String operation;

    private LocalDateTime lastSend;

    private LocalDateTime nextSchedule;

    private Vector<Command> childs = new Vector<>();

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(getClass())
                .add("code", code)
                .add("child", childs)
                .toString();
    }

}

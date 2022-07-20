package cl.architeq.zkpush.impl.command;

import lombok.NonNull;

public class REBOOTCommand extends Command {

    private static final long serialVersionUID = 1L;

    REBOOTCommand() {
    }

    public REBOOTCommand(@NonNull String deviceSN, @NonNull String code) {
        setDeviceSN(deviceSN);
        setCode(code);
    }

    @Override
    public String getCommandString() {

        StringBuilder builder = new StringBuilder();
        builder.append("C:");
        builder.append(getCode());
        builder.append(":REBOOT");

        return builder.toString();
    }

}

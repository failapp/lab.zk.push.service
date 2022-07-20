package cl.architeq.zkpush.impl.command;

import lombok.NonNull;

public class GenericCommand extends Command {

    private static final long serialVersionUID = 1L;

    private String command;

    public GenericCommand() {
    }

    public GenericCommand(@NonNull String deviceSN, @NonNull String code, @NonNull String command) {
        setDeviceSN(deviceSN);
        setCode(code);
        this.command = command;
    }

    @Override
    public String getCommandString() {

        StringBuilder builder = new StringBuilder();
        builder.append("C:");
        builder.append(getCode());
        builder.append(":").append(this.command);

        return builder.toString();
    }

}

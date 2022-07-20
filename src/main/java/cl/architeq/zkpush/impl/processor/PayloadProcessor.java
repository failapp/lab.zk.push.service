package cl.architeq.zkpush.impl.processor;

import cl.architeq.zkpush.model.Device;

public interface PayloadProcessor {

    public int execute(Device device, String payload);

}

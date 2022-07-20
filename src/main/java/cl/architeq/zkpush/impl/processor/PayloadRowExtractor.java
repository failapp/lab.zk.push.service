package cl.architeq.zkpush.impl.processor;

import cl.architeq.zkpush.model.Device;

public interface PayloadRowExtractor {

    public void extract(Device device, String row);

}

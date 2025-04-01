package device.sensor;

import device.Device;
import protocol.Protocol;

public abstract class Sensor extends Device {
    public Sensor(Protocol protocol, int deviceId) {
        super(protocol, deviceId);
    }

    public abstract String getSensType();

    public abstract String data2String();

    @Override
    public String getDevType() {
        return getSensType() + " Sensor";
    }
}
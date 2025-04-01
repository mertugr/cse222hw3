package device.sensor;

import protocol.Protocol;

public abstract class TempSensor extends Sensor {
    public TempSensor(Protocol protocol, int deviceId) {
        super(protocol, deviceId);
    }

    public float getTemp() {
        protocol.read();
        return (float) (Math.random() * 40); // Random temperature between 0-40°C
    }

    @Override
    public String getSensType() {
        return "TempSensor";
    }

    @Override
    public String data2String() {
        return String.format("Temp: %.2f", getTemp());
    }
}
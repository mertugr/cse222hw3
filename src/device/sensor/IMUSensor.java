package device.sensor;

import protocol.Protocol;

public abstract class IMUSensor extends Sensor {
    public IMUSensor(Protocol protocol, int deviceId) {
        super(protocol, deviceId);
    }

    public float getAccel() {
        protocol.read();
        return (float) (Math.random() * 10); // Random acceleration
    }

    public float getRot() {
        protocol.read();
        return (float) (Math.random() * 360); // Random rotation
    }

    @Override
    public String getSensType() {
        return "IMUSensor";
    }

    @Override
    public String data2String() {
        return String.format("Accel: %.2f, Rot: %.2f", getAccel(), getRot());
    }
}
package device;

import protocol.Protocol;

public abstract class Device {
    protected Protocol protocol;
    protected State state = State.OFF;
    protected int deviceId;

    public Device(Protocol protocol, int deviceId) {
        this.protocol = protocol;
        this.deviceId = deviceId;
    }

    public abstract void turnON();

    public abstract void turnOFF();

    public abstract String getName();

    public abstract String getDevType();

    public State getState() {
        return state;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public int getDeviceId() {
        return deviceId;
    }
}
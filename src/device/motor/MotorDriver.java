package device.motor;

import device.Device;
import device.State;
import protocol.Protocol;

public abstract class MotorDriver extends Device {
    public MotorDriver(Protocol protocol, int deviceId) {
        super(protocol, deviceId);
    }

    public void setMotorSpeed(int speed) {
        if (state == State.ON) {
            protocol.write("Speed set to: " + speed);
        } else {
            System.out.println("Error: MotorDriver is OFF");
        }
    }

    @Override
    public String getDevType() {
        return "MotorDriver";
    }
}
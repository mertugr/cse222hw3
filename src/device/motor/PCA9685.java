package device.motor;

import device.State;
import protocol.I2C;
import protocol.Protocol;

public class PCA9685 extends MotorDriver {
    public PCA9685(Protocol protocol, int deviceId) {
        super(protocol, deviceId);
        if (!(protocol instanceof I2C)) {
            throw new IllegalArgumentException("PCA9685 only supports I2C protocol");
        }
    }

    @Override
    public void turnON() {
        System.out.println(getName() + ": Turning ON");
        state = State.ON;
        protocol.write("turnON");
    }

    @Override
    public void turnOFF() {
        System.out.println(getName() + ": Turning OFF");
        state = State.OFF;
        protocol.write("turnOFF");
    }

    @Override
    public String getName() {
        return "PCA9685";
    }
}
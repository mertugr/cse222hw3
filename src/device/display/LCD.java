package device.display;

import device.State;
import protocol.I2C;
import protocol.Protocol;

public class LCD extends Display {
    public LCD(Protocol protocol, int deviceId) {
        super(protocol, deviceId);
        if (!(protocol instanceof I2C)) {
            throw new IllegalArgumentException("LCD only supports I2C protocol");
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
        return "LCD";
    }
}
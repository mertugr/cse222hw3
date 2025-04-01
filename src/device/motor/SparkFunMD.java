package device.motor;

import device.State;
import protocol.Protocol;
import protocol.SPI;

public class SparkFunMD extends MotorDriver {
    public SparkFunMD(Protocol protocol, int deviceId) {
        super(protocol, deviceId);
        if (!(protocol instanceof SPI)) {
            throw new IllegalArgumentException("SparkFunMD only supports SPI protocol");
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
        return "SparkFunMD";
    }
}
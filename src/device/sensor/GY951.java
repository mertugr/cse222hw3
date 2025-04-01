package device.sensor;

import device.State;
import protocol.Protocol;
import protocol.SPI;
import protocol.UART;

public class GY951 extends IMUSensor {
    public GY951(Protocol protocol, int deviceId) {
        super(protocol, deviceId);
        if (!(protocol instanceof SPI || protocol instanceof UART)) {
            throw new IllegalArgumentException("GY951 only supports SPI or UART protocols");
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
        return "GY951";
    }
}
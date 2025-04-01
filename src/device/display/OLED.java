package device.display;

import device.State;
import protocol.Protocol;
import protocol.SPI;

public class OLED extends Display {
    public OLED(Protocol protocol, int deviceId) {
        super(protocol, deviceId);
        if (!(protocol instanceof SPI)) {
            throw new IllegalArgumentException("OLED only supports SPI protocol");
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
        return "OLED";
    }
}
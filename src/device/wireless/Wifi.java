package device.wireless;

import device.State;
import protocol.Protocol;
import protocol.SPI;
import protocol.UART;

public class Wifi extends WirelessIO {
    public Wifi(Protocol protocol, int deviceId) {
        super(protocol, deviceId);
        if (!(protocol instanceof SPI || protocol instanceof UART)) {
            throw new IllegalArgumentException("Wifi only supports SPI or UART protocols");
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
        return "Wifi";
    }
}
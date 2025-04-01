package device.wireless;

import device.State;
import protocol.Protocol;
import protocol.UART;

public class Bluetooth extends WirelessIO {
    public Bluetooth(Protocol protocol, int deviceId) {
        super(protocol, deviceId);
        if (!(protocol instanceof UART)) {
            throw new IllegalArgumentException("Bluetooth only supports UART protocol");
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
        return "Bluetooth";
    }
}
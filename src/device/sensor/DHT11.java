package device.sensor;

import device.State;
import protocol.OneWire;
import protocol.Protocol;

public class DHT11 extends TempSensor {
    public DHT11(Protocol protocol, int deviceId) {
        super(protocol, deviceId);
        if (!(protocol instanceof OneWire)) {
            throw new IllegalArgumentException("DHT11 only supports OneWire protocol");
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
        return "DHT11";
    }
}
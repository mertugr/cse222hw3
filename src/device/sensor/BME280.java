package device.sensor;

import device.State;
import protocol.I2C;
import protocol.Protocol;
import protocol.SPI;

public class BME280 extends TempSensor {
    public BME280(Protocol protocol, int deviceId) {
        super(protocol, deviceId);
        if (!(protocol instanceof I2C || protocol instanceof SPI)) {
            throw new IllegalArgumentException("BME280 only supports I2C or SPI protocols");
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
        return "BME280";
    }
}
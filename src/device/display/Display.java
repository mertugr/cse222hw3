package device.display;

import device.Device;
import device.State;
import protocol.Protocol;

public abstract class Display extends Device {
    public Display(Protocol protocol, int deviceId) {
        super(protocol, deviceId);
    }

    public void printData(String data) {
        if (state == State.ON) {
            protocol.write(data);
        } else {
            System.out.println("Error: Display is OFF");
        }
    }

    @Override
    public String getDevType() {
        return "Display";
    }
}
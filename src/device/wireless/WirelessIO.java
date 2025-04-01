package device.wireless;

import device.Device;
import device.State;
import protocol.Protocol;

public abstract class WirelessIO extends Device {
    public WirelessIO(Protocol protocol, int deviceId) {
        super(protocol, deviceId);
    }

    public String recvData() {
        if (state == State.ON) {
            return protocol.read();
        } else {
            System.out.println("Error: WirelessIO is OFF");
            return null;
        }
    }

    public void sendData(String data) {
        if (state == State.ON) {
            protocol.write(data);
        } else {
            System.out.println("Error: WirelessIO is OFF");
        }
    }

    @Override
    public String getDevType() {
        return "WirelessIO";
    }
}
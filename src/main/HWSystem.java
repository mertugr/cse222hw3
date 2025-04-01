package main;

import java.util.ArrayList;

import device.Device;
import device.State;
import device.display.Display;
import device.display.LCD;
import device.display.OLED;
import device.motor.MotorDriver;
import device.motor.PCA9685;
import device.motor.SparkFunMD;
import device.sensor.BME280;
import device.sensor.DHT11;
import device.sensor.GY951;
import device.sensor.MPU6050;
import device.sensor.Sensor;
import device.wireless.Bluetooth;
import device.wireless.Wifi;
import device.wireless.WirelessIO;
import protocol.I2C;
import protocol.OneWire;
import protocol.Protocol;
import protocol.SPI;
import protocol.UART;

public class HWSystem {
    private ArrayList<Protocol> ports;
    private ArrayList<Device> devices;
    private int maxSensors;
    private int maxDisplays;
    private int maxWirelessIO;
    private int maxMotorDrivers;

    public HWSystem(ArrayList<Protocol> ports, int maxSensors, int maxDisplays, int maxWirelessIO,
            int maxMotorDrivers) {
        this.ports = ports;
        this.devices = new ArrayList<>();
        this.maxSensors = maxSensors;
        this.maxDisplays = maxDisplays;
        this.maxWirelessIO = maxWirelessIO;
        this.maxMotorDrivers = maxMotorDrivers;
    }

    public boolean addDevice(String devName, int portID, int devID) {
        // Check if portID exists
        if (portID < 0 || portID >= ports.size()) {
            System.out.println("Port does not exist.");
            System.out.println("Command failed.");
            return false;
        }

        // Check if port is already occupied
        for (Device device : devices) {
            if (getPortForDevice(device) == portID) {
                System.out.println("Port is already occupied.");
                System.out.println("Command failed.");
                return false;
            }
        }

        Protocol protocol = ports.get(portID);
        Device newDevice = null;

        try {
            // Create the device based on its name
            switch (devName) {
                case "DHT11":
                    checkDeviceLimit("Sensor");
                    checkDeviceID("Sensor", devID);
                    if (protocol instanceof OneWire) {
                        newDevice = new DHT11(protocol, devID);
                    } else {
                        System.out.println("DHT11 is not compatible with " + protocol.getProtocolName());
                        System.out.println("Command failed.");
                        return false;
                    }
                    break;
                case "BME280":
                    checkDeviceLimit("Sensor");
                    checkDeviceID("Sensor", devID);
                    if (protocol instanceof I2C || protocol instanceof SPI) {
                        newDevice = new BME280(protocol, devID);
                    } else {
                        System.out.println("BME280 is not compatible with " + protocol.getProtocolName());
                        System.out.println("Command failed.");
                        return false;
                    }
                    break;
                case "MPU6050":
                    checkDeviceLimit("Sensor");
                    checkDeviceID("Sensor", devID);
                    if (protocol instanceof I2C) {
                        newDevice = new MPU6050(protocol, devID);
                    } else {
                        System.out.println("MPU6050 is not compatible with " + protocol.getProtocolName());
                        System.out.println("Command failed.");
                        return false;
                    }
                    break;
                case "GY951":
                    checkDeviceLimit("Sensor");
                    checkDeviceID("Sensor", devID);
                    if (protocol instanceof SPI || protocol instanceof UART) {
                        newDevice = new GY951(protocol, devID);
                    } else {
                        System.out.println("GY951 is not compatible with " + protocol.getProtocolName());
                        System.out.println("Command failed.");
                        return false;
                    }
                    break;
                case "LCD":
                    checkDeviceLimit("Display");
                    checkDeviceID("Display", devID);
                    if (protocol instanceof I2C) {
                        newDevice = new LCD(protocol, devID);
                    } else {
                        System.out.println("LCD is not compatible with " + protocol.getProtocolName());
                        System.out.println("Command failed.");
                        return false;
                    }
                    break;
                case "OLED":
                    checkDeviceLimit("Display");
                    checkDeviceID("Display", devID);
                    if (protocol instanceof SPI) {
                        newDevice = new OLED(protocol, devID);
                    } else {
                        System.out.println("OLED is not compatible with " + protocol.getProtocolName());
                        System.out.println("Command failed.");
                        return false;
                    }
                    break;
                case "Bluetooth":
                    checkDeviceLimit("WirelessIO");
                    checkDeviceID("WirelessIO", devID);
                    if (protocol instanceof UART) {
                        newDevice = new Bluetooth(protocol, devID);
                    } else {
                        System.out.println("Bluetooth is not compatible with " + protocol.getProtocolName());
                        System.out.println("Command failed.");
                        return false;
                    }
                    break;
                case "Wifi":
                    checkDeviceLimit("WirelessIO");
                    checkDeviceID("WirelessIO", devID);
                    if (protocol instanceof SPI || protocol instanceof UART) {
                        newDevice = new Wifi(protocol, devID);
                    } else {
                        System.out.println("Wifi is not compatible with " + protocol.getProtocolName());
                        System.out.println("Command failed.");
                        return false;
                    }
                    break;
                case "PCA9685":
                    checkDeviceLimit("MotorDriver");
                    checkDeviceID("MotorDriver", devID);
                    if (protocol instanceof I2C) {
                        newDevice = new PCA9685(protocol, devID);
                    } else {
                        System.out.println("PCA9685 is not compatible with " + protocol.getProtocolName());
                        System.out.println("Command failed.");
                        return false;
                    }
                    break;
                case "SparkFunMD":
                    checkDeviceLimit("MotorDriver");
                    checkDeviceID("MotorDriver", devID);
                    if (protocol instanceof SPI) {
                        newDevice = new SparkFunMD(protocol, devID);
                    } else {
                        System.out.println("SparkFunMD is not compatible with " + protocol.getProtocolName());
                        System.out.println("Command failed.");
                        return false;
                    }
                    break;
                default:
                    System.out.println("Unknown device type: " + devName);
                    System.out.println("Command failed.");
                    return false;
            }

            devices.add(newDevice);
            return true;

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            System.out.println("Command failed.");
            return false;
        }
    }

    private void checkDeviceLimit(String devType) {
        int count = countDevicesByType(devType);
        int limit = 0;

        switch (devType) {
            case "Sensor":
                limit = maxSensors;
                break;
            case "Display":
                limit = maxDisplays;
                break;
            case "WirelessIO":
                limit = maxWirelessIO;
                break;
            case "MotorDriver":
                limit = maxMotorDrivers;
                break;
        }

        if (count >= limit) {
            throw new IllegalArgumentException("Maximum number of " + devType + " devices reached");
        }
    }

    private void checkDeviceID(String devType, int devID) {
        // Check if the devID is valid (non-negative and within range)
        if (devID < 0) {
            throw new IllegalArgumentException("Device ID cannot be negative");
        }
        
        int maxID;
        switch (devType) {
            case "Sensor":
                maxID = maxSensors;
                break;
            case "Display":
                maxID = maxDisplays;
                break;
            case "WirelessIO":
                maxID = maxWirelessIO;
                break;
            case "MotorDriver":
                maxID = maxMotorDrivers;
                break;
            default:
                maxID = 0;
        }
        
        if (devID >= maxID) {
            throw new IllegalArgumentException("Device ID must be between 0 and " + (maxID - 1) + " for " + devType);
        }
        
        // Check if the devID is already in use for this type
        for (Device device : devices) {
            if (device.getDevType().contains(devType) && device.getDeviceId() == devID) {
                throw new IllegalArgumentException("Device ID " + devID + " is already in use for " + devType);
            }
        }
    }

    public boolean removeDevice(int portID) {
        if (portID < 0 || portID >= ports.size()) {
            System.out.println("Port does not exist.");
            System.out.println("Command failed.");
            return false;
        }

        Device deviceToRemove = null;
        for (Device device : devices) {
            if (getPortForDevice(device) == portID) {
                deviceToRemove = device;
                break;
            }
        }

        if (deviceToRemove == null) {
            System.out.println("No device connected to port " + portID);
            System.out.println("Command failed.");
            return false;
        }

        if (deviceToRemove.getState() == State.ON) {
            System.out.println("Device is active.");
            System.out.println("Command failed.");
            return false;
        }

        devices.remove(deviceToRemove);
        return true;
    }

    public void turnON(int portID) {
        Device device = getDeviceByPort(portID);
        if (device != null) {
            device.turnON();
        } else {
            System.out.println("No device connected to port " + portID);
            System.out.println("Command failed.");
        }
    }

    public void turnOFF(int portID) {
        Device device = getDeviceByPort(portID);
        if (device != null) {
            device.turnOFF();
        } else {
            System.out.println("No device connected to port " + portID);
            System.out.println("Command failed.");
        }
    }

    public void readSensor(int devID) {
        Device device = getDeviceByTypeAndID("Sensor", devID);
        if (device != null) {
            if (device.getState() == State.ON) {
                if (device instanceof Sensor sensor) {
                    System.out.println(sensor.getName() + " " + sensor.getDevType() + ": " + sensor.data2String());
                }
            } else {
                System.out.println("Device is not active.");
                System.out.println("Command failed.");
            }
        } else {
            System.out.println("Sensor with ID " + devID + " not found");
            System.out.println("Command failed.");
        }
    }

    public void printDisplay(int devID, String data) {
        Device device = getDeviceByTypeAndID("Display", devID);
        if (device != null) {
            if (device.getState() == State.ON) {
                if (device instanceof Display display) {
                    display.printData(data);
                }
            } else {
                System.out.println("Device is not active.");
                System.out.println("Command failed.");
            }
        } else {
            System.out.println("Display with ID " + devID + " not found");
            System.out.println("Command failed.");
        }
    }

    public void readWireless(int devID) {
        Device device = getDeviceByTypeAndID("WirelessIO", devID);
        if (device != null) {
            if (device.getState() == State.ON) {
                if (device instanceof WirelessIO wireless) {
                    System.out.println(wireless.recvData());
                }
            } else {
                System.out.println("Device is not active.");
                System.out.println("Command failed.");
            }
        } else {
            System.out.println("WirelessIO with ID " + devID + " not found");
            System.out.println("Command failed.");
        }
    }

    public void writeWireless(int devID, String data) {
        Device device = getDeviceByTypeAndID("WirelessIO", devID);
        if (device != null) {
            if (device.getState() == State.ON) {
                if (device instanceof WirelessIO wireless) {
                    wireless.sendData(data);
                }
            } else {
                System.out.println("Device is not active.");
                System.out.println("Command failed.");
            }
        } else {
            System.out.println("WirelessIO with ID " + devID + " not found");
            System.out.println("Command failed.");
        }
    }

    public void setMotorSpeed(int devID, int speed) {
        Device device = getDeviceByTypeAndID("MotorDriver", devID);
        if (device != null) {
            if (device.getState() == State.ON) {
                if (device instanceof MotorDriver motor) {
                    motor.setMotorSpeed(speed);
                }
            } else {
                System.out.println("Device is not active.");
                System.out.println("Command failed.");
            }
        } else {
            System.out.println("MotorDriver with ID " + devID + " not found");
            System.out.println("Command failed.");
        }
    }

    public void listPorts() {
        System.out.println("list of ports:");
        for (int i = 0; i < ports.size(); i++) {
            Protocol protocol = ports.get(i);
            Device device = getDeviceByPort(i);

            if (device != null) {
                System.out.println(i + " " + protocol.getProtocolName() + " occupied " +
                        device.getName() + " " + device.getDevType() + " " +
                        device.getDeviceId() + " " + device.getState());
            } else {
                System.out.println(i + " " + protocol.getProtocolName() + " empty");
            }
        }
    }

    public void listDevicesByType(String devType) {
        System.out.println("list of " + devType + "s:");
        for (Device device : devices) {
            if (device.getDevType().contains(devType)) {
                int portID = getPortForDevice(device);
                System.out.println(device.getName() + " " + device.getDeviceId() + " " +
                        portID + " " + ports.get(portID).getProtocolName());
            }
        }
    }

    private Device getDeviceByPort(int portID) {
        for (Device device : devices) {
            if (getPortForDevice(device) == portID) {
                return device;
            }
        }
        return null;
    }

    private Device getDeviceByTypeAndID(String devType, int devID) {
        for (Device device : devices) {
            if (device.getDevType().contains(devType) && device.getDeviceId() == devID) {
                return device;
            }
        }
        return null;
    }

    private int getPortForDevice(Device device) {
        Protocol deviceProtocol = device.getProtocol();
        for (int i = 0; i < ports.size(); i++) {
            if (ports.get(i) == deviceProtocol) {
                return i;
            }
        }
        return -1;
    }

    private int countDevicesByType(String devType) {
        int count = 0;
        for (Device device : devices) {
            if (device.getDevType().contains(devType)) {
                count++;
            }
        }
        return count;
    }
}
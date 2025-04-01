package main;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import main.HWSystem;

import protocol.*;

public class Main {
    public static void main(String[] args) {
        try {
            // Read configuration file
            Scanner configScanner = new Scanner(new File("config.txt"));

            // Parse port configuration
            String portConfig = configScanner.nextLine().split(": ")[1];
            String[] portTypes = portConfig.split(",");
            ArrayList<Protocol> ports = new ArrayList<>();

            for (String portType : portTypes) {
                switch (portType.trim()) {
                    case "I2C":
                        ports.add(new I2C());
                        break;
                    case "SPI":
                        ports.add(new SPI());
                        break;
                    case "UART":
                        ports.add(new UART());
                        break;
                    case "OneWire":
                        ports.add(new OneWire());
                        break;
                    default:
                        System.out.println("Unknown protocol type: " + portType);
                        break;
                }
            }

            // Parse device limits
            int maxSensors = Integer.parseInt(configScanner.nextLine().split(":")[1].trim());
            int maxDisplays = Integer.parseInt(configScanner.nextLine().split(":")[1].trim());
            int maxWirelessIO = Integer.parseInt(configScanner.nextLine().split(":")[1].trim());
            int maxMotorDrivers = Integer.parseInt(configScanner.nextLine().split(":")[1].trim());

            configScanner.close();

            // Create the system
            HWSystem system = new HWSystem(ports, maxSensors, maxDisplays, maxWirelessIO, maxMotorDrivers);

            // Process user commands
            Scanner commandScanner = new Scanner(System.in);
            String command;

            System.out.println("Hardware System initialized. Enter commands (type 'exit' to quit):");

            while (true) {
                System.out.print("Command: ");
                command = commandScanner.nextLine().trim();

                if (command.equals("exit")) {
                    break;
                }

                processCommand(command, system);
            }

            commandScanner.close();
            System.out.println("System terminated.");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void processCommand(String command, HWSystem system) {
        try {
            String[] parts = command.split("\\s+");
            String cmd = parts[0];

            switch (cmd) {
                case "turnON":
                    if (parts.length != 2) {
                        System.out.println("Error: Invalid command format. Use 'turnON <portID>'");
                        System.out.println("Command failed.");
                        return;
                    }
                    int portID = Integer.parseInt(parts[1]);
                    system.turnON(portID);
                    break;

                case "turnOFF":
                    if (parts.length != 2) {
                        System.out.println("Error: Invalid command format. Use 'turnOFF <portID>'");
                        System.out.println("Command failed.");
                        return;
                    }
                    portID = Integer.parseInt(parts[1]);
                    system.turnOFF(portID);
                    break;

                case "addDev":
                    if (parts.length != 4) {
                        System.out.println("Error: Invalid command format. Use 'addDev <devName> <portID> <devID>'");
                        System.out.println("Command failed.");
                        return;
                    }
                    String devName = parts[1];
                    portID = Integer.parseInt(parts[2]);
                    int devID = Integer.parseInt(parts[3]);
                    system.addDevice(devName, portID, devID);
                    break;

                case "rmDev":
                    if (parts.length != 2) {
                        System.out.println("Error: Invalid command format. Use 'rmDev <portID>'");
                        System.out.println("Command failed.");
                        return;
                    }
                    portID = Integer.parseInt(parts[1]);
                    system.removeDevice(portID);
                    break;

                case "list":
                    if (parts.length != 2) {
                        System.out.println("Error: Invalid command format. Use 'list ports' or 'list <devType>'");
                        System.out.println("Command failed.");
                        return;
                    }
                    if (parts[1].equals("ports")) {
                        system.listPorts();
                    } else {
                        system.listDevicesByType(parts[1]);
                    }
                    break;

                case "readSensor":
                    if (parts.length != 2) {
                        System.out.println("Error: Invalid command format. Use 'readSensor <devID>'");
                        System.out.println("Command failed.");
                        return;
                    }
                    devID = Integer.parseInt(parts[1]);
                    system.readSensor(devID);
                    break;

                case "printDisplay":
                    if (parts.length < 3) {
                        System.out.println("Error: Invalid command format. Use 'printDisplay <devID> <String>'");
                        System.out.println("Command failed.");
                        return;
                    }
                    devID = Integer.parseInt(parts[1]);
                    // Reconstruct the message from remaining parts
                    StringBuilder message = new StringBuilder();
                    for (int i = 2; i < parts.length; i++) {
                        message.append(parts[i]);
                        if (i < parts.length - 1) {
                            message.append(" ");
                        }
                    }
                    system.printDisplay(devID, message.toString());
                    break;

                case "readWireless":
                    if (parts.length != 2) {
                        System.out.println("Error: Invalid command format. Use 'readWireless <devID>'");
                        System.out.println("Command failed.");
                        return;
                    }
                    devID = Integer.parseInt(parts[1]);
                    system.readWireless(devID);
                    break;

                case "writeWireless":
                    if (parts.length < 3) {
                        System.out.println("Error: Invalid command format. Use 'writeWireless <devID> <String>'");
                        System.out.println("Command failed.");
                        return;
                    }
                    devID = Integer.parseInt(parts[1]);
                    // Reconstruct the message from remaining parts
                    message = new StringBuilder();
                    for (int i = 2; i < parts.length; i++) {
                        message.append(parts[i]);
                        if (i < parts.length - 1) {
                            message.append(" ");
                        }
                    }
                    system.writeWireless(devID, message.toString());
                    break;

                case "setMotorSpeed":
                    if (parts.length != 3) {
                        System.out.println("Error: Invalid command format. Use 'setMotorSpeed <devID> <integer>'");
                        System.out.println("Command failed.");
                        return;
                    }
                    devID = Integer.parseInt(parts[1]);
                    int speed = Integer.parseInt(parts[2]);
                    system.setMotorSpeed(devID, speed);
                    break;

                default:
                    System.out.println("Error: Unknown command '" + cmd + "'");
                    System.out.println("Command failed.");
                    break;
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid number format");
            System.out.println("Command failed.");
        } catch (Exception e) {
            System.out.println("Error processing command: " + e.getMessage());
            System.out.println("Command failed.");
        }
    }
}
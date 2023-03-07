package src.main.util;

import javax.bluetooth.*;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import java.util.UUID;

import java.io.*;

public class BluetoothExample {
  public static void main(String[] args) throws Exception {
    // Get the local Bluetooth adapter
    LocalDevice localDevice = LocalDevice.getLocalDevice();
    System.out.println("Local Bluetooth adapter: " + localDevice.getBluetoothAddress());

    // Search for Bluetooth devices
    DiscoveryAgent agent = localDevice.getDiscoveryAgent();
    RemoteDevice[] devices = agent.retrieveDevices(DiscoveryAgent.PREKNOWN);

    for (RemoteDevice device : devices) {
      System.out.println("Found device: " + device.getBluetoothAddress() + " " + device.getFriendlyName(true));
    }

    // Establish a connection to a Bluetooth device
    String address = localDevice.getBluetoothAddress(); // Replace with the address of your device
    UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // Replace with the UUID of your service
    StreamConnection connection = (StreamConnection) Connector.open("btspp://" + address + ":1", Connector.READ_WRITE, true);
    System.out.println("Connected to " + connection);

    // Send and receive data
    InputStream in = connection.openInputStream();
    OutputStream out = connection.openOutputStream();

    out.write("Hello Bluetooth".getBytes());

    byte[] buffer = new byte[1024];
    int len = in.read(buffer);

    String received = new String(buffer, 0, len);
    System.out.println("Received: " + received);

    // Close the connection
    connection.close();
  }
}


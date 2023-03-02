package src.main.util;

import global.GlobalInfo;
import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

public class JsscMessageService {
    private SerialPort serialPort;

    public JsscMessageService(String portName) throws SerialPortException {
        // Create a new serial port object with the given port name
        serialPort = new SerialPort(portName);

        // Open the serial port
        serialPort.openPort();

        // Set the serial port parameters
        serialPort.setParams(
            SerialPort.BAUDRATE_9600,
            SerialPort.DATABITS_8,
            SerialPort.STOPBITS_1,
            SerialPort.PARITY_NONE
        );
    }

    public void send(String message) throws SerialPortException {
        // Convert the message to a byte array and send it through the serial port
        serialPort.writeBytes((message+"\n").getBytes());
    }

    public String receive() throws SerialPortException {
        // Read bytes from the serial port until a newline character is received
        StringBuilder messageBuilder = new StringBuilder();
        while (true) {
            byte[] buffer = serialPort.readBytes(1);
            if (buffer == null) {
                continue;
            }
            String character = new String(buffer);
            if (character.equals("\n")) {
                break;
            }
            messageBuilder.append(character);
        }

        // Return the received message as a string
        return messageBuilder.toString();
    }

    public void close() throws SerialPortException {
        // Close the serial port
        serialPort.closePort();
    }

    public static void main(String[] args) throws SerialPortException, InterruptedException {
        // Find the available serial ports
        String[] portNames = SerialPortList.getPortNames();
        if (portNames.length == 0) {
            System.out.println("No serial ports found");
            return;
        }

        // Use the first available serial port
        JsscMessageService messageService = new JsscMessageService(portNames[1]);
        System.out.println("Waiting Arduino for rebooting...");		
		Thread.sleep(4000);
		System.out.println("Ready.");	
        String msg;
        while(true){
			Thread.sleep(500);
			messageService.send(String.valueOf(GlobalInfo.getAdminControl()));
			//System.out.println("Control in the server: "+String.valueOf(GlobalInfo.getAdminControl()));
			msg = messageService.receive();
			System.out.println(msg);
			
//			msg = channel.receiveMsg();
//			System.out.println(msg);
		}
    }
}


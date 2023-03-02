package src.main.util;

import com.fazecast.jSerialComm.SerialPort;

import global.GlobalInfo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class SerialCommunication {

    private final SerialPort serialPort;
    private final InputStream inputStream;
    private final OutputStream outputStream;

    public SerialCommunication(String portName, int baudRate) {
        serialPort = SerialPort.getCommPort(portName);
        serialPort.setBaudRate(baudRate);
        serialPort.openPort();

        inputStream = serialPort.getInputStream();
        outputStream = serialPort.getOutputStream();
    }

    public void sendMsg(String data) throws IOException {
        outputStream.write((data + "\n").getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
    }

    public String receiveMsg() throws IOException {
    	try {
    		   while (true)
    		   {
    		      while (serialPort.bytesAvailable() == 0)
    		         Thread.sleep(20);

    		      byte[] readBuffer = new byte[serialPort.bytesAvailable()];
    		      int numRead = serialPort.readBytes(readBuffer, readBuffer.length);
    		      System.out.println("Read " + numRead + " bytes.");
    		   }
    		} catch (Exception e) { e.printStackTrace(); }
    	
    	return "";
    }

    public void close() {
        serialPort.closePort();
    }
    
    public static void main(String[] args) throws Exception{
		SerialPort[] portNames = SerialPort.getCommPorts();
		int i;
	  	for (i = 0; i < portNames.length; i++){
		    System.out.println(portNames[i].getSystemPortName());
		}
		SerialCommunication channel = new SerialCommunication(portNames[i-1].getSystemPortName(),9600);
		System.out.println("Waiting Arduino for rebooting...");		
		Thread.sleep(4000);
		System.out.println("Ready.");	
		String msg;
		msg = channel.receiveMsg();
		System.out.println(msg);
//		while(true){
//			Thread.sleep(500);
//			channel.sendMsg("PING");
//			channel.sendMsg(String.valueOf(GlobalInfo.getAdminControl()));
			//System.out.println("Control in the server: "+String.valueOf(GlobalInfo.getAdminControl()));
//			Thread.sleep(50);
//			msg = channel.receiveMsg();
//			System.out.println(msg);
//			channel.sendMsg(String.valueOf(GlobalInfo.getPresence()));
//			
//			msg = channel.receiveMsg();
//			System.out.println(msg);
//			channel.sendMsg(String.valueOf(GlobalInfo.getEnoughLight()));
//			
//			msg = channel.receiveMsg();
//			System.out.println(msg);
//			channel.sendMsg(String.valueOf(GlobalInfo.getLight()));
//			
//			msg = channel.receiveMsg();
//			System.out.println(msg);
//			
//			channel.sendMsg(String.valueOf(GlobalInfo.getAlpha()));
		//}
	}
}


package src.main.util;

import java.util.concurrent.*;

import global.GlobalInfo;
import io.vertx.core.json.JsonObject;
import jssc.*;

public class SerialCommChannel implements CommChannel, SerialPortEventListener {

	private SerialPort serialPort;
	private BlockingQueue<String> queue;
	private StringBuffer currentMsg = new StringBuffer("");
	
	public SerialCommChannel(String port, int rate) throws Exception {
		queue = new ArrayBlockingQueue<String>(100);

		serialPort = new SerialPort(port);
		serialPort.openPort();

		serialPort.setParams(rate,
		                         SerialPort.DATABITS_8,
		                         SerialPort.STOPBITS_1,
		                         SerialPort.PARITY_NONE);

		serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN | 
		                                  SerialPort.FLOWCONTROL_RTSCTS_OUT);

		// serialPort.addEventListener(this, SerialPort.MASK_RXCHAR);
		serialPort.addEventListener(this);
	}

	@Override
	public void sendMsg(String msg) {
		char[] array = (msg+"\n").toCharArray();
		byte[] bytes = new byte[array.length];
		for (int i = 0; i < array.length; i++){
			bytes[i] = (byte) array[i];
		}
		try {
			synchronized (serialPort) {
				for(int i = 0;i<bytes.length;i++) {
					serialPort.writeByte(bytes[i]);
					Thread.sleep(6);
				}
				//serialPort.writeBytes(bytes);
			}
		} catch(Exception ex){
			ex.printStackTrace();
		}
	}

	@Override
	public String receiveMsg() throws InterruptedException {
		// TODO Auto-generated method stub
		return queue.take();
	}

	@Override
	public boolean isMsgAvailable() {
		// TODO Auto-generated method stub
		return !queue.isEmpty();
	}

	/**
	 * This should be called when you stop using the port.
	 * This will prevent port locking on platforms like Linux.
	 */
	public void close() {
		try {
			if (serialPort != null) {
				serialPort.removeEventListener();
				serialPort.closePort();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}


	public void serialEvent(SerialPortEvent event) {
		/* if there are bytes received in the input buffer */
		if (event.isRXCHAR()) {
            try {
            		String msg = serialPort.readString(event.getEventValue());
            		
            		msg = msg.replaceAll("\r", "");
            		
            		currentMsg.append(msg);
            		
            		boolean goAhead = true;
            		
        			while(goAhead) {
        				String msg2 = currentMsg.toString();
        				int index = msg2.indexOf("\n");
            			if (index >= 0) {
            				queue.put(msg2.substring(0, index));
            				currentMsg = new StringBuffer("");
            				if (index + 1 < msg2.length()) {
            					currentMsg.append(msg2.substring(index + 1)); 
            				}
            			} else {
            				goAhead = false;
            			}
        			}
        			
            } catch (Exception ex) {
            		ex.printStackTrace();
                System.out.println("Error in receiving string from COM-port: " + ex);
            }
        }
	}
	
	public static void main(String[] args) throws Exception{
		String[] portNames = SerialPortList.getPortNames();
		int i;
	  	for (i = 0; i < portNames.length; i++){
		    System.out.println(portNames[i]);
		}
		SerialCommChannel channel = new SerialCommChannel(portNames[i-1],9600);
		System.out.println("Waiting Arduino for rebooting...");		
		Thread.sleep(4000);
		System.out.println("Ready.");	
		String msg;
		while(true){
			//Thread.sleep(200);
			JsonObject jsonObject = new JsonObject();
			jsonObject.put("presence", GlobalInfo.getPresence());
			jsonObject.put("enoughLight", GlobalInfo.getEnoughLight());
			jsonObject.put("adminControl", GlobalInfo.getAdminControl());
			jsonObject.put("alpha", GlobalInfo.getAlpha());
			jsonObject.put("lightControl", GlobalInfo.getLight());
			jsonObject.put("time", GlobalInfo.getTime());
			channel.sendMsg(jsonObject.encode());
					
			if(channel.isMsgAvailable()) {
				msg = channel.receiveMsg();
				System.out.println("the message: "+msg);
			}
		}
	}

}

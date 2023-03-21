package src.main.util;

import org.eclipse.paho.client.mqttv3.*;

import global.GlobalInfo;

public class MQTTClient {

    public static void run() throws MqttException, InterruptedException {
    	System.out.println("Creating MQTT Client...");
        // Create a new MQTT client instance
        MqttClient client = new MqttClient("ssl://790e9765770b4666b4cfe938fdb1b081.s2.eu.hivemq.cloud:8883", MqttClient.generateClientId());

        // Set the username and password for the connection
        String username = "merkp";
        String password = "Marcos28";
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        options.setKeepAliveInterval(60);
        options.setUserName(username);
        options.setPassword(password.toCharArray());

        // Connect to the MQTT broker
        client.connect(options);

        // Subscribe to a topic
        String topic1 = "isEnoughLight";
        String topic2 = "Presence";
        client.subscribe(topic1);
        client.subscribe(topic2);
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {}

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                //System.out.println("Received message on topic " + topic + ": " + message.toString());
                if(topic.equals("Presence")) {
                	//System.out.println(message.toString().equals("1"));
                	GlobalInfo.updatePresence(message.toString().equals("1"));
                }else if(topic.equals("isEnoughLight")) {
                	GlobalInfo.updateEnoughLight(message.toString().equals("1"));
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {}
        });

        // Wait for messages to arrive
        //Thread.sleep(Long.MAX_VALUE);

    }

}


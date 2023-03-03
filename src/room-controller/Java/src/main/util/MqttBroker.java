package src.main.util;

import global.GlobalInfo;
import io.vertx.core.Vertx;
import io.vertx.mqtt.MqttServer;

public class MqttBroker {
	public static void start() {
	    Vertx vertx = Vertx.vertx();

	    MqttServer mqttServer = MqttServer.create(vertx);

	    mqttServer.endpointHandler(endpoint -> {
	      endpoint.publishHandler(message -> {
	    	if(message.topicName().equals("presence")) {
	    		GlobalInfo.updatePresence(Boolean.parseBoolean(message.payload().toString()));
	    		System.out.println(GlobalInfo.getPresence());
	    	}else if(message.topicName().equals("light")) {
	    		GlobalInfo.updateEnoughLight(Boolean.parseBoolean(message.payload().toString()));
	    		System.out.println(GlobalInfo.getEnoughLight());
	    	}
	        System.out.println("Received message on topic " + message.topicName() + " with payload " + message.payload());
	      });

	      endpoint.accept(true);
	    });

	    mqttServer.listen(1883, "localhost", ar -> {
	      if (ar.succeeded()) {
	        System.out.println("MQTT server started");
	      } else {
	        System.err.println("MQTT server failed to start");
	      }
	    });
	  }
}

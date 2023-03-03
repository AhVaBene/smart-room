package src.main.util;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.mqtt.MqttClient;

/*
 * MQTT Agent
 */
public class MQTTAgent extends AbstractVerticle {
	
	public MQTTAgent() {
	}

	@Override
	public void start() {		
		MqttClient client = MqttClient.create(vertx);

		client.connect(1883, "localhost", c -> {

			log("connected");
			
			log("subscribing...");
			client.publishHandler(s -> {
			  System.out.println("There are new message in topic: " + s.topicName());
			  System.out.println("Content(as string) of the message: " + s.payload().toString());
			  System.out.println("QoS: " + s.qosLevel());
			})
			.subscribe("prova", 2);		

			log("publishing a msg");
			client.publish("presence",
				  Buffer.buffer("false"),
				  MqttQoS.AT_LEAST_ONCE,
				  false,
				  false);
			client.publish("light",
					  Buffer.buffer("false"),
					  MqttQoS.AT_LEAST_ONCE,
					  false,
					  false);
		});
	}
	

	private void log(String msg) {
		System.out.println("[MQTT AGENT] "+msg);
	}

}
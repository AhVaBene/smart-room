package com.example.starter;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import global.GlobalInfo;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import starter.HandlerDashboard;
import jssc.*;
import src.main.util.JsscMessageService;
import src.main.util.SerialCommChannel;

public class MyHttpServer extends AbstractVerticle {

    @Override
    public void start() {
        HttpServer server = vertx.createHttpServer();
        HandlerDashboard handler = new HandlerDashboard();
        Router router = Router.router(vertx);
        router.post("/esp").handler(this::handleEspRequest);
        router.post("/control").handler(this::handleControlRequest);
        router.post("/setAlpha").handler(this::handleAlphaRequest);
        router.post("/setLight").handler(this::handleLightRequest);
        router.get("/lightState").handler(this::handleLightStateRequest);
        router.get("/rollerState").handler(this::handleRollerStateRequest);
        router.get("/lightConsumption").handler(this::handleLightConsumptionRequest);
        router.get("/").handler(handler);

        server.requestHandler(router);

        server.listen(8080);
    }
    /*@doc
     * Esp request it has to be a post request with 2 fields:
     * presence:(Boolean) indicates wheter there is someone in the room
     * enoughLight:(Boolean) if the light level is greater than a threshold
     * */
    private void handleEspRequest(RoutingContext routingContext) {
        HttpServerRequest request = routingContext.request();
        request.bodyHandler(buffer -> {
            String json = buffer.toString();
            JsonObject jsonObject = new JsonObject(json);
            Map<String, Object> map = jsonObject.getMap();
            Map<String, Boolean> finalMap = map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e-> Boolean.valueOf(String.valueOf(e.getValue()))));
            System.out.println(finalMap);
            GlobalInfo.updateLightHours(finalMap.get("enoughLight"));
            GlobalInfo.updatePresence(finalMap.get("presence"));
            //System.out.println(GlobalInfo.getDurationLight());
            HttpServerResponse response = request.response();
            response.putHeader("content-type", "plain/text");
            response.end("Response");
        });
    }
    
    
    private void handleLightStateRequest(RoutingContext routingContext) {
    	HttpServerResponse response = routingContext.response();
    	response.putHeader("content-type", "application/json");
    	
    	JsonObject jsonObject = new JsonObject();
        jsonObject.put("lightState", GlobalInfo.getCurrentLight());
        response.end(jsonObject.encode());        
    }
    
    private void handleRollerStateRequest(RoutingContext routingContext) {
    	HttpServerResponse response = routingContext.response();
    	response.putHeader("content-type", "application/json");
    	
    	JsonObject jsonObject = new JsonObject();
        jsonObject.put("alpha", GlobalInfo.getCurrentAlpha());
        response.end(jsonObject.encode());    
    }
    
    private void handleLightConsumptionRequest(RoutingContext routingContext) {
    	HttpServerResponse response = routingContext.response();
    	response.putHeader("content-type", "application/json");
    	
    	JsonObject jsonObject = new JsonObject();
    	
    	JsonArray jsonArray = new JsonArray();
    	
    	for (Map.Entry<String, Long> e : GlobalInfo.getDurationLight().entrySet()) {
    		JsonObject obj = new JsonObject();
    		obj.put("time", e.getKey());
    		obj.put("hours", e.getValue());
    		jsonArray.add(obj);
    		
    	}
        jsonObject.put("array", jsonArray);
        response.end(jsonObject.encode());
        
    }
    
    private void handleControlRequest(RoutingContext routingContext) {
    	HttpServerRequest request = routingContext.request();
        request.bodyHandler(buffer -> {
        	String json = buffer.toString();
            JsonObject jsonObject = new JsonObject(json);
            Map<String, Object> map = jsonObject.getMap();
            Map<String, Boolean> finalMap = map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e-> Boolean.valueOf(String.valueOf(e.getValue()))));
        	GlobalInfo.setControl(finalMap.get("control"));
        	
            HttpServerResponse response = request.response();
            response.putHeader("content-type", "text/plain");
            response.end("Received a POST request");
        });
    }
    
    private void handleAlphaRequest(RoutingContext routingContext) {
    	if(GlobalInfo.getAdminControl()) {
	    	HttpServerRequest request = routingContext.request();
	        request.bodyHandler(buffer -> {
	        	String json = buffer.toString();
	            JsonObject jsonObject = new JsonObject(json);
	            Map<String, Object> map = jsonObject.getMap();
	            Map<String, Integer> finalMap = map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e-> Integer.valueOf(String.valueOf(e.getValue()))));
	        	GlobalInfo.setAlpha(finalMap.get("alpha"));
	        	//System.out.println(GlobalInfo.getAlpha());
	            HttpServerResponse response = request.response();
	            response.putHeader("content-type", "text/plain");
	            response.end("Received a POST request");
	        });
    	}
    }
    
    private void handleLightRequest(RoutingContext routingContext) {
    	if(GlobalInfo.getAdminControl()) {
	    	HttpServerRequest request = routingContext.request();
	        request.bodyHandler(buffer -> {
	        	String json = buffer.toString();
	            JsonObject jsonObject = new JsonObject(json);
	            Map<String, Object> map = jsonObject.getMap();
	            Map<String, String> finalMap = map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e-> String.valueOf(e.getValue())));
	        	GlobalInfo.setLight(finalMap.get("light"));
	        	//System.out.println(GlobalInfo.getLight());
	            HttpServerResponse response = request.response();
	            response.putHeader("content-type", "text/plain");
	            response.end("Received a POST request");
	        });
    	}
    }
    
    public static void main(String[] args) throws Exception{
    	Vertx vertx = Vertx.vertx();
		MyHttpServer service = new MyHttpServer();
		vertx.deployVerticle(service);
		
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
				try {
				JsonObject jsonObj = new JsonObject(msg.trim());
				GlobalInfo.setCurrentAlpha(Integer.parseInt(jsonObj.getValue("alpha").toString()));
				GlobalInfo.setCurrentLight(Boolean.parseBoolean(jsonObj.getValue("light").toString()));
				}catch(Exception e) {
					System.out.println("Error in parsing the json reques");
				}
			}
		}
	}
		
}




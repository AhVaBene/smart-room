package com.example.starter;

import java.util.Map;
import java.util.stream.Collectors;

import global.GlobalInfo;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class MyHttpServer extends AbstractVerticle {

    @Override
    public void start() {
        HttpServer server = vertx.createHttpServer();

        Router router = Router.router(vertx);
        router.post("/esp").handler(this::handleEspRequest);

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
            System.out.println(GlobalInfo.getDurationLight());
            HttpServerResponse response = request.response();
            response.putHeader("content-type", "plain/text");
            response.end("Response");
        });
    }
    
    public static void main(String[] args) {
    	Vertx vertx = Vertx.vertx();
		MyHttpServer service = new MyHttpServer();
		vertx.deployVerticle(service);
    }
}




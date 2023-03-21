#include "Led.h"
#include "LightSensorImpl.h"
#include "PirImpl.h"
#include "PubSubClient.h"

#include <WiFi.h>
#include <HTTPClient.h>
#include <WiFiClientSecure.h>

const int led_pin = 4;
const int light_sensor_pin = 5;
const int pir_pin = 6;

const int threshold = 400;
bool is_detected = false;
bool is_enough_light = false;

TaskHandle_t LedTask;
TaskHandle_t LightSensorTask;
TaskHandle_t PirTask;

const char* ssid = "Prova";
const char* password = "aragosta";
const char* mqttServer = "790e9765770b4666b4cfe938fdb1b081.s2.eu.hivemq.cloud";
//const char* mqttServer = "broker.hivemq.com";
const int mqttPort = 8883;
const char* mqttUser = "merkp";
const char* mqttPassword = "Marcos28";

const char *serviceURI = "http://192.168.71.220:8080/";
WiFiClientSecure espClient;
PubSubClient client(espClient);

void connectToWifi(const char* ssid, const char* password){
  WiFi.begin(ssid, password);
  Serial.println("Connecting");
  while(WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("");
  Serial.print("Connected to WiFi network with IP Address: ");
  Serial.println(WiFi.localIP());
}

int sendData(String address, bool enoughLight, bool presence){  
  
   HTTPClient http;    
   http.begin(address + "esp");      
   http.addHeader("Content-Type", "application/json");    
    
   String msg = 
    String("{ \"enoughLight\": ") + String(enoughLight) + 
    ", \"presence\": \"" + String(presence) +"\" }";
   
   int retCode = http.POST(msg);   
   http.end();  
      
   return retCode;
}

Light *led = new Led(led_pin);
LightSensor *light_sensor = new LightSensorImpl(light_sensor_pin);
Pir *pir = new PirImpl(pir_pin);

void setup() {
  delay(500);
  Serial.begin(115200);
  xTaskCreatePinnedToCore(LedTaskCode, "LedTask", 10000, NULL, 1, &LedTask, 0);
  delay(500);
  xTaskCreatePinnedToCore(LightSensorTaskCode, "LightSensorTask", 10000, NULL, 1, &LightSensorTask, 1);
  delay(500);
  xTaskCreatePinnedToCore(PirTaskCode, "PirTask", 10000, NULL, 1, &PirTask, 1);      
  delay(500);
  connectToWifi(ssid, password);
  connectMQTTBroker();
}

void connectMQTTBroker() {
  espClient.setInsecure();
  client.setServer(mqttServer, mqttPort);
  client.setCallback(callback);

  while (!client.connected()) {
    if (client.connect("ESP32Client", mqttUser, mqttPassword)) {
      Serial.println("Connected to MQTT broker");
    } else {
      Serial.print("Failed to connect to MQTT broker, state=");
      Serial.print(client.state());
      Serial.println(" try again in 5 seconds");
      delay(5000);
    }
  }
}

void callback(char* topic, byte* payload, unsigned int length) {
  
}

void publish(const char* topic, const char* message) {
  if (client.publish(topic, message)) {
    //Serial.println("Publish Successful");
  } else {
    Serial.println("Publish Failed");
  }
}

void LedTaskCode( void * param ){
  Serial.print("LedTask is running on core ");
  Serial.println(xPortGetCoreID());
  while(1){
    if(is_detected){
      led->switchOn();
    }else{
      led->switchOff();
    }
    delay(1000);
  }
}

void LightSensorTaskCode( void * param ){
  Serial.print("LightSensor is running on core ");
  Serial.println(xPortGetCoreID());
  while(1){
    is_enough_light = light_sensor->getLightIntensity() >= threshold;
    delay(1000);
  }
}

void PirTaskCode( void * param ){
  Serial.print("PirTask is running on core ");
  Serial.println(xPortGetCoreID());
  while(1){
    is_detected = pir->isDetected();
    delay(1000);
  }
}

void loop() {
  client.loop();

  if (client.connected()) {
    //Serial.println("publishing...");
    char lightStr[10], mvmStr[10];
    snprintf(lightStr, 2, "%d", is_enough_light);
    snprintf(mvmStr, 2, "%d", is_detected);

    publish("isEnoughLight", lightStr);
    publish("Presence", mvmStr);

    delay(1000); 
  }else{
    //Serial.println("not publishing...");
  }
}

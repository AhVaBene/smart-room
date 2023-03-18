#include <Arduino.h>
#include "data/Scheduler.h"
#include "data/globals.h"
#include "data/LightSensorTask.h"
#include "data/PirTask.h"
#include "data/LedTask.h"
#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <WiFiClient.h>

#define LED_PIN D5
#define PIR_PIN D2
#define LS_PIN A0

const char* ssid = "Prova";
const char* password = "aragosta";

const char *serviceURI = "http://192.168.1.7:8080/";
  IPAddress server(192,168,1,7);

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

WiFiClient wifiClient;

void sendData(String address, bool enoughLight,bool presence){  
  
   HTTPClient http;    
   if(wifiClient.connect(server, 8080)){
     Serial.println("Funziona");
   }else{
     Serial.println("no");
   }
   http.begin(wifiClient, address);      
   http.addHeader("Content-Type", "application/json");    
    
   String msg = 
    String("{ \"enoughLight\": ") + String(enoughLight) + 
    ", \"presence\": \"" + String(presence) +"\" }";
   
   int retCode = http.POST(msg);   
   http.end();

   if(retCode > 0){
     Serial.println("ok");
   }else{
     Serial.println(http.errorToString(retCode).c_str());
   }
}

bool is_detected=false;
bool is_enough_light = false;
Scheduler sched;


void setup() {
  Serial.begin(9600);
  connectToWifi(ssid, password);
  sched.init(100);
  Task *t0 = new PirTask(PIR_PIN);
  t0->init(200);
  sched.addTask(t0);

 Task *t1 = new LightSensorTask(LS_PIN);
 t1->init(200);
 sched.addTask(t1);

 Task *t2 = new LedTask(LED_PIN);
 t2->init(200);
 sched.addTask(t2);
}

void loop() {
  sched.schedule();
  if (WiFi.status()== WL_CONNECTED){      
    sendData(String("http://192.168.1.7/"), is_enough_light, is_detected);
   }
  //Serial.println("It works!!");
}

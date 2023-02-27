#include "Led.h"
#include "lightSensorImpl.h"
#include "pirImpl.h"

const int led_1 = 4;
const int light_sensor = 36;
const int pir_pin = 5;

TaskHandle_t LedTask;
 
Light *led = new Led(led_1);
LightSensor *sensor = new LightSensorImpl(light_sensor);
Pir *pir = new PirImpl(pir_pin);

void setup() {
  Serial.begin(115200);

  xTaskCreatePinnedToCore(LedTaskcode,"LedTask",10000,NULL,1,&LedTask,0);                         
  delay(500); 
}

void LedTaskcode( void * l ){
  Serial.print("Task1 is running on core ");
  Serial.println(xPortGetCoreID());

  for(;;){
    Serial.println(sensor->getLightIntensity());
    if(sensor->getLightIntensity()>0){
      led->switchOff();
    }else{
      led->switchOn();
    }
    /*if(pir->isDetected()){
      led->switchOff();
    }else{
      led->switchOn();
    }*/
  } 
}

void loop() {
  Serial.print("this is the main loop running on core ");
  Serial.println(xPortGetCoreID());

}

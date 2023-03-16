#include "Led.h"
#include "LightSensorImpl.h"
#include "PirImpl.h"
#include "enums.h"

const int led_pin = 4;
const int light_sensor_pin = 5;
const int pir_pin = 6;

const int threshold = 50;
bool is_detected = false;
bool is_enough_light = false;

TaskHandle_t LedTask;
TaskHandle_t LightSensorTask;
TaskHandle_t PirTask;
 
Light *led = new Led(led_pin);
LightSensor *light_sensor = new LightSensorImpl(light_sensor_pin);
Pir *pir = new PirImpl(pir_pin);

void setup() {
  xTaskCreatePinnedToCore(LedTaskCode, "LedTask", 10000, NULL, 1, &LedTask, 1);
  delay(500);
  xTaskCreatePinnedToCore(LightSensorTaskCode, "LightSensorTask", 10000, NULL, 1, &LightSensorTask, 0);
  delay(500);
  xTaskCreatePinnedToCore(PirTaskCode, "PirTask", 10000, NULL, 1, &PirTask, 1);      
  delay(500); 
}

void LedTaskCode( void * param ){
  for(;;){
    switch (led_task_state) {
      case OFF:
          if(is_detected){
              led_task_state = ON;
              led->switchOn();
          }
          break;
      case ON:
          if(!is_detected){
              led_task_state = OFF;
              led->switchOff();
          }
          break;
    }
  }
}

void LightSensorTaskCode( void * param ){
  for(;;) {
    switch (light_sensor_task_state) {
    case DARK:
        Serial.println(light_sensor->getLightIntensity());
        if(light_sensor->getLightIntensity() >= threshold){            
            is_enough_light = true;
            light_sensor_task_state = LIGHT;
        }
        break;
    case LIGHT:
        if(light_sensor->getLightIntensity() < threshold){
            is_enough_light = false;
            light_sensor_task_state = DARK;
        }
        break;
    }
  }
}

void PirTaskCode( void * param ){
  for(;;) {
    switch (pir_task_state) {
    case NOT_DETECTED:
      Serial.println(pir->isDetected());
        if(pir->isDetected()){
            pir_task_state = DETECTED;
            is_detected = true;
        }
        break;
    
    case DETECTED:
        if(!pir->isDetected()){
            pir_task_state = NOT_DETECTED;
            is_detected = false;
        }
        break;
    }
  }
}

void loop() {

}

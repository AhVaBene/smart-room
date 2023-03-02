#include "LedTask.h"

LedTask::LedTask(int led_pin){
  this->led_pin = led_pin;
}

void LedTask::init(int period){
    this->led = new Led(this->led_pin);
    this->state = OFF;
    this->period = period;
}

void LedTask::task(){
  switch (this->state){
    case OFF:
      //Serial.println("The LED IS OFF: "+String(is_detected)+" "+String(is_enough_light));
        this->state = ON;
        this->led->switchOn();
      break;
        
    case ON:
      //Serial.println("The light is on");
        this->state = OFF;
        this->led->switchOff();
      break;
  }
}

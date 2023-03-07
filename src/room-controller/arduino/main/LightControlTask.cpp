#include "data/LightControlTask.h"

LightControlTask::LightControlTask(int led_pin){
  this->led_pin = led_pin;
}

void LightControlTask::init(int period){
    Task::init(period);
    this->led = new Led(this->led_pin);
    this->state = OFF;
    this->led->switchOff();
}

void LightControlTask::tick(){
  previousLightState = lightState;
  switch(this->state){
    case OFF:
      if(control || androidControl){
        if(lightControl){
          this->led->switchOn();
          this->state = ON;
          lightState = true;
        }
      }else if((isSomeonePresent && !isEnoughLight)){
        this->led->switchOn();
        this->state = ON;
        lightState = true;
      }
    break;
    case ON:
      if(control || androidControl){
        if(!lightControl){
          this->led->switchOff();
          this->state = OFF;
          lightState = false;
        }
      }else if(!isSomeonePresent || isEnoughLight){
        this->led->switchOff();
        this->state = OFF;
        lightState = false;
      }
    break;

  /**/
  }

}
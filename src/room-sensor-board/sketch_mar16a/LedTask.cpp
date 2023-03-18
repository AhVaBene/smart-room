#include "Arduino.h"
#include "data/LedTask.h"

LedTask::LedTask(int led_pin){
    this->led_pin = led_pin;
}

void LedTask::init(int period){
    Task::init(period);
    this->led = new Led(this->led_pin);
    this->state = OFF;
    this->led->switchOff();
}

void LedTask::tick(){
    switch (this->state)
    {
    case OFF:
        if(is_detected){
            this->state = ON;
            this->led->switchOn();
        }
        break;
    case ON:
        if(!is_detected){
            this->state = OFF;
            this->led->switchOff();
        }
        break;
    }
}
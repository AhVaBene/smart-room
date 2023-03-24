#include "Arduino.h"
#include "data/LightSensorTask.h"

LightSensorTask::LightSensorTask(int ls_pin){
    this->ls_pin = ls_pin;
}

void LightSensorTask::init(int period){
    Task::init(period);
    this->ls = new LightSensorImpl(this->ls_pin);
    this->state = DARK;
}

void LightSensorTask::tick(){
    switch (this->state)
    {
    case DARK:
        if(this->ls->getLightIntensity() >= this->threshold){
            is_enough_light = true;
            this->state = LIGHT;
        }
        break;
    case LIGHT:
        if(this->ls->getLightIntensity() < this->threshold){
            is_enough_light = false;
            this->state = DARK;
        }
        break;
    }
}
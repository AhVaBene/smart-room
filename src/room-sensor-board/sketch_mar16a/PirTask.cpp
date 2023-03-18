#include "Arduino.h"
#include "data/PirTask.h"

PirTask::PirTask(int pir_pin){
    this->pir_pin = pir_pin;
}

void PirTask::init(int period){
    Task::init(period);
    this->pir = new PirImpl(this->pir_pin);
    this->state = NOT_DETECTED;
}

void PirTask::tick(){
    switch (this->state)
    {
    case NOT_DETECTED:
        if(this->pir->isDetected()){
            this->state = DETECTED;
            is_detected = true;
        }
        break;
    
    case DETECTED:
        if(!this->pir->isDetected()){
            this->state = NOT_DETECTED;
            is_detected = false;
        }
        break;
    }
}

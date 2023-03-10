#include "data/RollerBlindsTask.h"

RollerBlindsTask::RollerBlindsTask(int servo_pin){
    this->servo_pin = servo_pin;
}

void RollerBlindsTask::init(int period){
    Task::init(period);
    this->servo = new ServoMotorImpl(this->servo_pin);
    this->servo->on();
    this->state = DOWN;
    alpha = ROLLED_DOWN;
    this->servo->setPosition(ROLLED_DOWN);
}
void RollerBlindsTask::tick(){
  previousAlpha = alpha;
  switch(this->state){
    case DOWN:
      if(control || androidControl){
        this->state = FREE;
        this->servo->setPosition(alpha);
      }else if(isSomeonePresent && time>800 && time < 1900){
        this->state = UP;
        this->servo->setPosition(ROLLED_UP);
        alpha = ROLLED_UP;
      }
    break;
    case UP:
      if(control || androidControl){
        this->state = FREE;
        this->servo->setPosition(alpha);
      }else if(!isSomeonePresent && (time>1900 || time <800)){
        this->state = DOWN;
        this->servo->setPosition(ROLLED_DOWN);
        alpha = ROLLED_DOWN;
      }
    break;
    case FREE:
      this->servo->setPosition(alpha);
      if(!control && !androidControl && (isSomeonePresent && time>800 && time < 1900)){
        this->state = UP;
        this->servo->setPosition(ROLLED_UP);
        alpha = ROLLED_UP;
      }else if(!control && !androidControl && (!isSomeonePresent && (time>1900 || time <800))){
        this->state = DOWN;
        this->servo->setPosition(ROLLED_DOWN);
        alpha = ROLLED_DOWN;
      }else if(!control && !androidControl){
        this->state = DOWN;
        this->servo->setPosition(ROLLED_DOWN);
        alpha = ROLLED_DOWN;
      }
    break;
  }
}
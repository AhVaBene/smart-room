#include "ServoMotorImpl.h"
#include "Arduino.h"
ServoMotorImpl::ServoMotorImpl(int pin){
  this->pin = pin;  
} 

void ServoMotorImpl::on(){
  motor.attach(pin);    
}

void ServoMotorImpl::setPosition(int angle){
  long a = map(angle, 0, 180, 750, 2250);
  //Serial.println("Angolo: "+String(a));
  motor.write(a);              
}

void ServoMotorImpl::off(){
  motor.detach();    
}
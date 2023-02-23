#include "PirImpl.h"
#include "Arduino.h"

PirImpl::PirImpl(int pin){
  this->pin = pin;
  pinMode(pin, INPUT);   
  for(int i = 0; i < CALIBRATION_TIME_SEC; i++){
    delay(1000);
  } 
} 
  
bool PirImpl::isDetected(){
  int detected = digitalRead(pin);
  return detected == HIGH;
  }
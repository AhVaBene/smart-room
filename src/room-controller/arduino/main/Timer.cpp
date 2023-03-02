#include "data/Timer.h"
#include "Arduino.h"

volatile bool flag;

void isr(void){
    flag = true;
}

Timer::Timer(){
    flag = false;
}

void Timer::setupInitialFreq(unsigned long period){
    Timer1.initialize(period);
}

void Timer::setupPeriod(unsigned long period){
    //Serial.println(period);
    Timer1.setPeriod(period);
    Timer1.attachInterrupt(isr);
}

void Timer::waitForNextTick(){
    bool flagLoop = false;
    while(!flagLoop){
        noInterrupts();
        flagLoop = flag;
        interrupts();
    }
    noInterrupts();
    flag = false;
    interrupts();
}
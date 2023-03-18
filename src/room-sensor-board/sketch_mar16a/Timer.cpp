#include "data/Timer.h"
#include "Arduino.h"

volatile bool flag;

void isr(void){
    flag = true;
}

Ticker timer1;

Timer::Timer(){
    flag = false;
}

void Timer::setupInitialFreq(unsigned long period){
    //ticker1.initialize(period);
}

void Timer::setupPeriod(unsigned long period){
    timer1.attach_ms(100, isr);
    
}

void Timer::waitForNextTick(){
    bool flagLoop = false;
    while(!flagLoop){
        noInterrupts();
        flagLoop = flag;
        interrupts();
        yield();
    }
    flag = false;
}
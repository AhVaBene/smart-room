#ifndef TIMER_H
#define TIMER_H

#include<TimerOne.h>

class Timer{
    public:
    Timer();
    void setupInitialFreq(unsigned long period);
    void setupPeriod(unsigned long period);
    void waitForNextTick();
};

#endif
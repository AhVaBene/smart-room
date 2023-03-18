#ifndef TIMER_H
#define TIMER_H

#include "Ticker.h"

class Timer{
    public:
    Timer();
    void setupInitialFreq(unsigned long period);
    void setupPeriod(unsigned long period);
    void waitForNextTick();
};

#endif
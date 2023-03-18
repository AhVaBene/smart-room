#ifndef __LEDA_TASK_H__
#define __LEDA_TASK_H__

#include "Task.h"
#include "Led.h"
#include "globals.h"
class LedTask : public Task{
    int led_pin;

    int period;

    Light *led;

    enum{ON, OFF} state;

public:

    LedTask(int led_pin);
    void init(int period);
    void tick();

};

#endif
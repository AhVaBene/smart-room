#ifndef __LIGHTCONTROL_TASK_H__
#define __LIGHTCONTROL_TASK_H__

#include "Task.h"
#include "Led.h"
#include "globals.h"
class LightControlTask : public Task{
    int led_pin;

    Light *led;

    enum{ON, OFF} state;

public:

    LightControlTask(int led_pin);
    void init(int period);
    void tick();

};

#endif
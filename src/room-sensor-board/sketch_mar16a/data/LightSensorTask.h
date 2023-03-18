#ifndef __LS_TASK_H__
#define __LS_TASK_H__

#include "Task.h"
#include "LightSensorImpl.h"
#include "globals.h"
class LightSensorTask : public Task{
    int ls_pin;

    const int threshold = 50;

    LightSensor *ls;

    enum{LIGHT, DARK} state;

public:

    LightSensorTask(int ls_pin);
    void init(int period);
    void tick();

};

#endif
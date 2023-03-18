#ifndef __PIR_TASK_H__
#define __PIR_TASK_H__

#include "Task.h"
#include "PirImpl.h"
#include "globals.h"
class PirTask : public Task{
    int pir_pin;

    Pir *pir;

    enum{DETECTED, NOT_DETECTED} state;


public:

    PirTask(int pir_pin);
    void init(int period);
    void tick();

};

#endif
#ifndef __INPUTCONTROL_TASK_H__
#define __INPUTCONTROL_TASK_H__

#include "Task.h"
#include "globals.h"
class InputControlTask : public Task{

    enum{IDLE, MANUAL} state;

public:

    InputControlTask();
    void init(int period);
    void tick();

};

#endif
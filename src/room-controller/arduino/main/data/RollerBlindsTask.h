#ifndef __ROLLERBLINDS_TASK_H__
#define __ROLLERBLINDS_TASK_H__

#include "Task.h"
#include "ServoMotorImpl.h"
#include "globals.h"

#define ROLLED_UP 0
#define ROLLED_DOWN 180
class RollerBlindsTask : public Task{
    int servo_pin;

    ServoMotor *servo;

    enum{UP, DOWN, FREE} state;

public:

    RollerBlindsTask(int servo_pin);
    void init(int period);
    void tick();

};

#endif
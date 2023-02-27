#ifndef __LED_TASK_H__
#define __LED_TASK_H__
#include "FreeRTOS.h"
#include "Led.h"

class LedTask{
    int led_pin;
    int period;
    Light *led;
    enum{ON, OFF} state;


    public:
      LedTask(int led_pin);
      void init(int period);
      void task();
};

#endif

#ifndef ENUMS_H_
#define ENUMS_H_

enum LedTaskState{
  ON, OFF
};

enum LightSensorTaskState{
  LIGHT, DARK
};

enum PirTaskState{
  NOT_DETECTED, DETECTED
};

extern LedTaskState led_task_state;
extern LightSensorTaskState light_sensor_task_state;
extern PirTaskState pir_task_state;

#endif
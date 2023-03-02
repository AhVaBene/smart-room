#include <Arduino.h>
#include "data/Scheduler.h"
#include "data/MsgServiceBT.h"
#include "data/globals.h"
#include "data/LightControlTask.h"
#include "data/RollerBlindsTask.h"
#include "data/InputControlTask.h"

#define LIGHT_PIN 5
#define SERVO_PIN 4
#define RX 2
#define TX 3

bool control=false;
bool lightControl = false;
bool rollerBlindsControl = false;
bool isSomeonePresent = false;
bool isEnoughLight = false;
int alpha = 180;
int time = 0;
bool lightState = false;
bool androidControl = false;
bool previousLightState = false;
int previousAlpha = 180;
MsgServiceBTClass *msgManagerBT = new MsgServiceBTClass(RX, TX);
MessageService *msgManager = new MessageService(9600);

Scheduler sched;
/*Speed up the control process*/
/*The task under 200ms doesn't let the server update the values of the global class*/
void setup() {
  //Serial.begin(9600);
  msgManager->begin();
  msgManagerBT->init();
  Timer1.initialize(10);
  sched.init(100);

   Task *t0 = new InputControlTask();
   t0->init(200);
   sched.addTask(t0);

   Task *t1 = new LightControlTask(LIGHT_PIN);
   t1->init(200);
   sched.addTask(t1);

   Task *t2 = new RollerBlindsTask(SERVO_PIN);
   t2->init(200);
   sched.addTask(t2);
}

void loop() {
  sched.schedule();
}

#ifndef __PIRIMPL__
#define __PIRIMPL__

#include "Pir.h"

#define CALIBRATION_TIME_SEC 10

class PirImpl: public Pir {
 
public: 
  PirImpl(int pin);
  bool isDetected();

private:
  int pin;
};

#endif
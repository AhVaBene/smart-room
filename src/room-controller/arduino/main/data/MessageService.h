#ifndef __MESSAGESERVICE_H__
#define __MESSAGESERVICE_H__
#include "Arduino.h"
class MessageService{
  private:
    int baudRate;
    String incomingMessage;
  
  public:
    MessageService(int baudRate);

    void begin();

    void send(String message);
    bool available();

    String receive();
};

#endif
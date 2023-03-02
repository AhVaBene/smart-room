#include "Arduino.h"
#include "data/MsgServiceBT.h"


MsgServiceBTClass::MsgServiceBTClass(int rxPin, int txPin){
  channel = new SoftwareSerial(rxPin, txPin);
}

void MsgServiceBTClass::init(){
  content.reserve(256);
  channel->begin(9600);
  availableMsg = NULL;
}

bool MsgServiceBTClass::sendMsg(Msg msg){
  channel->println(msg.getContent());  
}

bool MsgServiceBTClass::isMsgAvailable(){
  while (channel->available()) {
    char ch = (char) channel->read();
    if (ch == '\n'){
      availableMsg = new Msg(content); 
      content = "";
      return true;    
    } else {
      content += ch;      
    }
  }
  return false;  
}

Msg* MsgServiceBTClass::receiveMsg(){
  if (availableMsg != NULL){
    Msg* msg = availableMsg;
    availableMsg = NULL;
    return msg;  
  } else {
    return NULL;
  }
}
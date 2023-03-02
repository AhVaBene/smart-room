#include "data/MessageService.h"

MessageService::MessageService(int baudRate) {
  this->baudRate = baudRate;
  incomingMessage = "";
}

void MessageService::begin() {
  Serial.begin(baudRate);
}

void MessageService::send(String message) {
  Serial.println(message);
}

bool MessageService::available() {
  return Serial.available();
}

String MessageService::receive() {
  while (Serial.available()) {
    char c = Serial.read();
    if (c == '\n') {
      String message = incomingMessage;
      incomingMessage = "";
      return message;
    } else {
      incomingMessage += c;
    }
  }
  return "";
}
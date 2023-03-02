#include "data/InputControlTask.h"
#include "Arduino_JSON.h"
InputControlTask::InputControlTask(){}

void InputControlTask::init(int period){
  Task::init(period);
  this->state = IDLE;

}
/*missing bluetooth*/
void InputControlTask::tick(){
  //msgManagerBT->sendMsg(String("CTRL"));
  String msg;
  JSONVar respServer;
  JSONVar respBT;
  JSONVar req;
  msg = msgManager->receive();
  if(msg.length() != 0){
    respServer = JSON.parse(msg);
    if(JSON.typeof(respServer) == "undefined"){
      msgManager->send(String("Bad structure"));
    }
    // else{
    //   msgManager->send(msg);
    // }
  }

  msg = msgManagerBT->receiveMsg()->getContent();
  if(msg.length() != 0){
    respBT = JSON.parse(msg);
    if(JSON.typeof(respBT) == "undefined"){
      msgManagerBT->sendMsg(String("Bad structure"));
    }
    // else{
    //   msgManagerBT->sendMsg(msg);
    // }
  }

  if(respServer.hasOwnProperty("adminControl")){
    control = (bool) respServer["adminControl"];
  }
  if(respBT.hasOwnProperty("adroidControl")){
    androidControl = (bool) respBT["androidControl"];
  }
  /*we give more priority to the admin dashboard respect to the application*/
  if(control){
    androidControl = false;
  }
  if(androidControl){
    control = true;
  }
  switch(this->state){
    case IDLE:
      if(control){
        if(androidControl){
          if(respBT.hasOwnProperty("lightControl")){
            lightControl = (bool) respBT["lightControl"];
          }
          if(respBT.hasOwnProperty("alpha")){
            alpha = (int) respBT["alpha"];
          }
        }else{
          if(respServer.hasOwnProperty("lightControl")){
            lightControl = (bool) respServer["lightControl"];
          }
          if(respServer.hasOwnProperty("alpha")){
            alpha = (int) respServer["alpha"];
          }
        }
        this->state = MANUAL;
      }else{
        if(respServer.hasOwnProperty("presence")){
          isSomeonePresent = (bool) respServer["presence"];
        }
        if(respServer.hasOwnProperty("enoughLight")){
          isEnoughLight = (bool) respServer["enoughLight"];
        }
        if(respServer.hasOwnProperty("time")){
          String s =  respServer["time"];//hour*100 + minutes
          time = (s.substring(0,2).toInt()*100 + s.substring(3).toInt());
        }
      }
    break;

    case MANUAL:
      if(!control){
        if(respServer.hasOwnProperty("presence")){
          isSomeonePresent = (bool) respServer["presence"];
        }
        if(respServer.hasOwnProperty("enoughLight")){
          isEnoughLight = (bool) respServer["enoughLight"];
        }
        if(respServer.hasOwnProperty("time")){
          String s =  respServer["time"];//hour*100 + minutes
          time = (s.substring(0,2).toInt()*100 + s.substring(3).toInt() );
        }
        this->state = IDLE;
        lightControl = false;
      }else{
        if(androidControl){
          if(respBT.hasOwnProperty("lightControl")){
            lightControl = (bool) respBT["lightControl"];
          }
          if(respBT.hasOwnProperty("alpha")){
            alpha = (int) respBT["alpha"];
          }
        }else{
          if(respServer.hasOwnProperty("lightControl")){
            lightControl = (bool) respServer["lightControl"];
          }
          if(respServer.hasOwnProperty("alpha")){
            alpha = (int) respServer["alpha"];
          }
        }
      }
    break;
  }
  if(previousLightState != lightState || previousAlpha != alpha){
    req["light"] = lightState;
    req["alpha"] = alpha;
    msgManager->send(JSON.stringify(req));
  }
}
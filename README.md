# smart-room
## Arduino setup
pin 4 Servo motor
pin 5 led
N.B 3 librerie esterne: TimerOne, ServoMotorTimer2, Arduino_JSON
## Java setup
Da eclipse apri il progetto java che sta nella cartella room-service
il programma principale è MyhttpServer.java
## API HTTP
- room-dashboard: http://localhost:8080/
- esp: inviare POST JSON ad http://localhost:8080/esp {presence:boolean, enoughLight:boolean} presence è vero se è presente qualcuno nella stanza, mentre enoughLight è vero se c'è abbastanza luce i.e. (livello della luce > threshold)
the http server is on http://localhost:8080

## API MQTT
Added the MQTT server on locahost:1883
- On topic "presence" invia boolean string e.g.("false"/"true") true se qualcuno è presente nella stanza
- On topic "light" invia boolean string e.g.("false"/"true") true se c'è abbastanza luce i.e. (livello della luce > threshold)

## API Bluetooth
-inviare periodicamente in formato json {androidControl:boolean, lightControl:boolean, alpha: int} androidControl è vero se si vuole prendere controllo del sistema da app, non ancora testato il funzionamento (Soggetto a possibili cambiamenti)

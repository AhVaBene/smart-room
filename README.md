# smart-room

Da eclipse apri il progetto java che sta nella cartella room-service
il programma principale è MyhttpServer.java
## API HTTP
- room-dashboard: http://localhost:8080/prova (sarà cambiato)
- esp: inviare POST JSON ad http://localhost:8080/esp {presence:boolean, enoughLight:boolean} presence è vero se è presente qualcuno nella stanza, mentre enoughLight è vero se c'è abbastanza luce i.e. (livello della luce > threshold)
the http server is on http://localhost:8080

## API MQTT
Missing

## API Bluetooth
-inviare periodicamente in formato json {androidControl:boolean, lightControl:boolean, alpha: int} androidControl è vero se si vole prender econtrollo del sistema da app, non ancora testato funzionamento (Soggetto a possibili cambiamenti)

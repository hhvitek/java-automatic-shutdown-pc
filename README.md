# java-automatic-shutdown-pc

Tak�e
Automatick� vyp�n�n� poc�tace v Jave a Swingu

MODEL

Task - abstraktn� tr�da pro podporovan� akce
- unik�tn� identifik�tor, jm�no, description
- akce - execute returns void / String on error raise TaskException
ShutdownTask, RestartTask

TaskModel - zde interfejs pro v�echny podporovan� akce
StateModel - dynamick� model/data pro UIko

VIEW

- dynamicky vytvoren� selektor pro v�ber akce - combo, radio - z TaskModelu
- casovac - spinner nebo text a dva butony - postup po 00:00 00:15 00:30 nebo jinak
- zobrazen� odpoctu coutdown k nule HH:MM:SS | cas kdy vypr�� cas 12:24
- ovl�d�n� - tlac�tka - Start, Zru�it, Ukoncit
- status bar - on error popup i status bar

NEPODPORUJEME v�ce akc� casovan�ch zaroven - v�dycky jenom jedna
StateModel - dve implementace objekty vs hibernate

LOGIKA

model pouze data
view odpoc�t�v� a pravidelne jednou za 1s refreshuje svoje data

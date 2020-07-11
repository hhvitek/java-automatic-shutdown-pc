# java-automatic-shutdown-pc

Takže
Automatické vypínání pocítace v Jave a Swingu

MODEL

Task - abstraktní trída pro podporované akce
- unikátní identifikátor, jméno, description
- akce - execute returns void / String on error raise TaskException
ShutdownTask, RestartTask

TaskModel - zde interfejs pro všechny podporované akce
StateModel - dynamický model/data pro UIko

VIEW

- dynamicky vytvorený selektor pro výber akce - combo, radio - z TaskModelu
- casovac - spinner nebo text a dva butony - postup po 00:00 00:15 00:30 nebo jinak
- zobrazení odpoctu coutdown k nule HH:MM:SS | cas kdy vyprší cas 12:24
- ovládání - tlacítka - Start, Zrušit, Ukoncit
- status bar - on error popup i status bar

NEPODPORUJEME více akcí casovaných zaroven - vždycky jenom jedna
StateModel - dve implementace objekty vs hibernate

LOGIKA

model pouze data
view odpocítává a pravidelne jednou za 1s refreshuje svoje data

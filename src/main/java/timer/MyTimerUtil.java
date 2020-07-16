package timer;


import org.jetbrains.annotations.NotNull;

import java.util.Timer;
import java.util.TimerTask;

public class MyTimerUtil implements MyTimer {

    private Timer timer;

    public MyTimerUtil() {
    }

    @Override
    public void scheduleAtFixedRate(@NotNull Runnable task, long rate) {
        TimerTask timerTask = wrapRunnableInTimerTask(task);

        timer = new Timer();
        timer.scheduleAtFixedRate(timerTask, 0L, rate);
    }

    private TimerTask wrapRunnableInTimerTask(@NotNull Runnable runnable) {
        return new TimerTask() {
            @Override
            public void run() {
                runnable.run();
            }
        };
    }

    @Override
    public boolean isRunning() {
        return timer != null;
    }

    @Override
    public void stop() {
        if (isRunning()) {
            timer.cancel();
            timer = null;
        }
    }
}

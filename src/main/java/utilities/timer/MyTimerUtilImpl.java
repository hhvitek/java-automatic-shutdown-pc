package utilities.timer;


import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;

/**
 * MyTimer's implementation using Java utils own Timer class.
 */
public class MyTimerUtilImpl implements MyTimer {

    private static final Logger logger = LoggerFactory.getLogger(MyTimer.class);

    private Timer timer;

    @Override
    public void scheduleAtFixedRate(@NotNull Runnable task, int rate) {
        if (timer != null) {
            logger.warn("Timer is already running. No another timer allowed.");
            return;
        }

        TimerTask timerTask = wrapRunnableInTimerTask(task);

        timer = new Timer();
        timer.scheduleAtFixedRate(timerTask, 0L, rate);
    }

    private @NotNull TimerTask wrapRunnableInTimerTask(@NotNull Runnable runnable) {
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

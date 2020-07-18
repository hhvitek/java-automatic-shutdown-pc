package utilities.timer;

import org.jetbrains.annotations.NotNull;

public interface MyTimer {

    /**
     * Schedules and starts the utilities.timer in periodic interval with period {@param rate}
     *
     * @param rate utilities.timer's period in millis.
     * @param task Runnable that is executed every period.
     */
    void scheduleAtFixedRate(@NotNull Runnable task, int rate);
    boolean isRunning();

    /**
     * Stops the utilities.timer if it is running and it cleans all resources.
     */
    void stop();
}

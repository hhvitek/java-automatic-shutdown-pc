package timer;

import org.jetbrains.annotations.NotNull;

public interface MyTimer {

    /**
     * Schedules and starts the timer in periodic interval with period {@param rate}
     *
     * @param rate timer's period in millis.
     * @param task Runnable that is executed every period.
     */
    void scheduleAtFixedRate(@NotNull Runnable task, long rate);
    boolean isRunning();

    /**
     * Stops the timer if it is running and it cleanes all resources.
     */
    void stop();
}

package utilities.timer;

import org.jetbrains.annotations.NotNull;

/**
 * To use periodic timer/clock in application
 * Define @rate as period in milliseconds and Runnable (such as method) to execute every period.
 */
public interface MyTimer {

    /**
     * Schedules and starts a timer in an interval with period {@param rate}
     *
     * @param rate timer's period in millis.
     * @param task Runnable that is executed every period.
     */
    void scheduleAtFixedRate(@NotNull Runnable task, int rate);

    boolean isRunning();

    /**
     * Stops the timer if it is running and should clean all resources.
     */
    void stop();
}

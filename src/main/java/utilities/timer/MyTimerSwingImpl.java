package utilities.timer;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * MyTimer's implementation using Swing's own Timer class.
 */
public class MyTimerSwingImpl implements MyTimer {

    private static final Logger logger = LoggerFactory.getLogger(MyTimer.class);

    private Timer timer;

    @Override
    public void scheduleAtFixedRate(@NotNull Runnable task, int rate) {
        if (timer != null) {
            logger.warn("Timer is already running. No another timer allowed.");
            return;
        }

        ActionListener listener = wrapRunnableInActionListener(task);

        timer = new Timer(rate, listener);
        timer.start();
    }

    private @NotNull ActionListener wrapRunnableInActionListener(@NotNull Runnable runnable) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
            timer.stop();
            timer = null;
        }
    }
}

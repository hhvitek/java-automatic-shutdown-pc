package timer;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyTimerSwing implements MyTimer {

    private Timer timer;

    public MyTimerSwing() {
    }

    @Override
    public void scheduleAtFixedRate(@NotNull Runnable task, long rate) {
        ActionListener listener = wrapRunnableInActionListener(task);

        timer = new Timer((int)rate, listener);
        timer.start();
    }

    private ActionListener wrapRunnableInActionListener(@NotNull Runnable runnable) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runnable.run();
            }
        };
    }

    @Override
    public boolean isRunning() {
        return timer != null && timer.isRunning();
    }

    @Override
    public void stop() {
        if (isRunning()) {
            timer.stop();
            timer = null;
        }
    }
}

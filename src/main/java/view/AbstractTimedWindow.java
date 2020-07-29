package view;

import controller.AbstractController;
import org.jetbrains.annotations.NotNull;
import utilities.timer.MyTimer;
import utilities.timer.MyTimerSwingImpl;

import javax.swing.*;
import java.awt.event.WindowAdapter;

public abstract class AbstractTimedWindow extends AbstractWindow {

    private static final int RATE_IN_MILLIS = 1000;

    private final MyTimer timer;

    protected AbstractTimedWindow(@NotNull AbstractController controller, @NotNull JFrame guiFrame) {
        super(controller, guiFrame);

        guiFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                stopTimer();
            }
        });

        timer = new MyTimerSwingImpl();
    }

    protected void startTimer() {
        timer.scheduleAtFixedRate(this::timerTick, RATE_IN_MILLIS);
    }

    protected void stopTimer() {
        timer.stop();
    }

    protected abstract void timerTick();

    @Override
    public void run() {
        super.run();
        startTimer();
    }
}

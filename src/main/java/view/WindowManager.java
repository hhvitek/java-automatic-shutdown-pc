package view;

import javax.swing.*;
import java.awt.*;

public class WindowManager {
    private JPanel panelGui;
    private JPanel panelChooseTasks;
    private JPanel panelChooseTiming;
    private JPanel panelCountdown;
    private JPanel panelControls;
    private JPanel panelStatusbar;
    private JLabel labelStatusbar;
    private JButton buttonSubmit;
    private JButton buttonExit;
    private JButton buttonCancel;
    private JSpinner spinnerChooseTiming;
    private JLabel labelWhenElapsed;
    private JLabel labelDurationDelay;

    private JFrame guiFrame;

    public WindowManager() {
        guiFrame = new JFrame("Vypnutí PC");
        guiFrame.setContentPane(panelGui);
        guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void run() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                guiFrame.pack();
                setCenteredToGoldenRatio(guiFrame);
                guiFrame.setVisible(true);
            }
        });
    }

    /**
     * Callable after swing frame.pack() function to center application window.
     */
    public static void setCenteredToGoldenRatio(JFrame frame) {
        Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
        int screenHeight = (int) screenDimension.getHeight();
        int screenWidth = (int) screenDimension.getWidth();

        int frameHeight = frame.getHeight();
        int frameWidth = frame.getWidth();

        int goldenRatioHeight = (int) ((screenHeight - frameHeight) * 0.38);

        frame.setLocation((screenWidth / 2) - (frameWidth / 2), goldenRatioHeight);
    }
}

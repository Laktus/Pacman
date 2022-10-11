package de.laktus.pacman;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static final boolean DEBUG = false;
    public static final double WIDTH_FACTOR = 0.7;
    public static final double HEIGHT_FACTOR = 0.7;

    public static void main(String... args) {
        final JFrame window = new JFrame();
        final Dimension dimension = getScreenSize();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize((int) (dimension.getWidth() * WIDTH_FACTOR), (int) (dimension.getHeight() * HEIGHT_FACTOR));
        window.setLocationRelativeTo(null);

        final ScreenManager screenManager = new ScreenManager();
        screenManager.push(new MenuScreen());
        screenManager.setFocusable(true);
        window.setContentPane(screenManager);
        window.setVisible(true);

        long UPS = 60;
        long FPS = 60;

        long initialTime = System.nanoTime();
        final double timeU = 1000000000 / UPS;
        final double timeF = 1000000000 / FPS;
        double deltaU = 0, deltaF = 0;
        int frames = 0, ticks = 0;
        long timer = System.currentTimeMillis();

        while (true) {
            long currentTime = System.nanoTime();
            deltaU += (currentTime - initialTime) / timeU;
            deltaF += (currentTime - initialTime) / timeF;
            initialTime = currentTime;

            if (deltaU >= 1) {
                screenManager.update();
                ticks++;
                deltaU--;
            }

            if (deltaF >= 1) {
                screenManager.repaint();
                frames++;
                deltaF--;
            }

            if (System.currentTimeMillis() - timer > 1000) {
                frames = 0;
                ticks = 0;
                timer += 1000;
            }
        }
    }

    public static Dimension getScreenSize() {
        return Toolkit.getDefaultToolkit().getScreenSize();
    }
}
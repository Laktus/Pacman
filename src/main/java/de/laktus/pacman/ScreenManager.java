package de.laktus.pacman;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Stack;

public class ScreenManager extends JPanel {
    private final Stack<Screen> screens;
    private Transition currentTransition;

    public ScreenManager() {
        this.screens = new Stack<>();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_BACK_SPACE:
                        pop(new FadeInTransition(60));
                        break;
                }
            }
        });
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                getCurrent().onComponentUpdate(e);
            }
        });
    }

    public void push(final Screen next) {
        if (!screens.isEmpty()) {
            final Screen current = screens.peek();
            this.removeKeyListener(current.getKeyListener());
        }
        next.setScreenManager(this);
        screens.push(next);
        this.addKeyListener(next.getKeyListener());
    }

    public void push(final Screen next, final Transition transition) {
        if (screens.isEmpty()) {
            return;
        }

        if (this.currentTransition == null || this.currentTransition.isCompleted()) {
            final Screen current = screens.peek();
            current.setPaused(true);
            this.removeKeyListener(current.getKeyListener());
            next.setScreenManager(this);
            next.setPaused(true);

            this.currentTransition = transition;
            this.currentTransition.setOnActivationListener(() -> {
                screens.push(next);
                next.onMounted(getSize());
                this.addKeyListener(next.getKeyListener());
            });
            this.currentTransition.setOnCompletionListener(() -> {
                currentTransition = null;
                next.setPaused(false);
            });
        }
    }

    public Screen getCurrent() {
        return screens.peek();
    }

    public Screen pop() {
        if (screens.size() == 1) {
            return null;
        }
        final Screen poppedScreen = screens.pop();
        this.removeKeyListener(poppedScreen.getKeyListener());
        final Screen current = screens.peek();
        this.addKeyListener(current.getKeyListener());
        return poppedScreen;
    }

    public void pop(final Transition transition) {
        if (screens.size() == 1) {
            return;
        }

        if (this.currentTransition == null || this.currentTransition.isCompleted()) {
            final Screen current = screens.peek();
            current.setPaused(true);
            this.removeKeyListener(current.getKeyListener());
            final Screen next = screens.elementAt(screens.size() - 2);
            next.setPaused(true);

            this.currentTransition = transition;
            this.currentTransition.setOnActivationListener(() -> {
                screens.pop();
                this.addKeyListener(next.getKeyListener());
            });
            this.currentTransition.setOnCompletionListener(() -> {
                currentTransition = null;
                next.setPaused(false);
            });
        }
    }

    @Override
    public void paintComponent(Graphics gOld) {
        final Graphics2D g = (Graphics2D) gOld;
        final Screen screen = screens.peek();
        screen.draw(g);

        if (currentTransition != null) {
            currentTransition.drawInternal(g, getSize());
        }
    }

    public void update() {
        final Screen screen = screens.peek();
        screen.updateInternal();

        if (currentTransition != null) {
            currentTransition.updateInternal();
        }
    }
}
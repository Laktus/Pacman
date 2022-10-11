package de.laktus.pacman;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyListener;

public abstract class Screen {
    protected ScreenManager screenManager;
    protected boolean paused;

    public void updateInternal() {
        if (!paused) {
            update();
        }
    }

    public abstract void update();

    public void draw(final Graphics2D g) {
        final View view = getView();
        if (view != null) {
            view.draw(screenManager.getSize(), g);
        }
    }

    public void setScreenManager(final ScreenManager screenManager) {
        this.screenManager = screenManager;
    }

    public ScreenManager getScreenManager() {
        return this.screenManager;
    }

    public void setPaused(final boolean paused) {
        this.paused = paused;
    }

    protected abstract View getView();

    public abstract KeyListener getKeyListener();

    void onMounted(final Dimension panelSize) {
    }

    void onComponentUpdate(final ComponentEvent componentEvent) {
    }
}
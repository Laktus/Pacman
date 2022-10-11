package de.laktus.pacman;

import java.awt.*;

public abstract class Transition {
    protected int ticks;
    protected final int maxTicks;
    protected boolean completed;
    protected boolean activated;
    private Runnable onCompletionListener;
    private Runnable onActivationListener;

    public Transition(final int maxTicks) {
        this.maxTicks = maxTicks;
    }

    public void drawInternal(final Graphics2D g, final Dimension panelSize) {
        if (!completed) {
            draw(g, panelSize);
        }
    }

    abstract void draw(Graphics2D g, Dimension panelSize);

    public void updateInternal() {
        if (!completed) {
            ticks++;
            update();

            if (!activated && getProgress() >= getActivationProgress()) {
                onActivationListener.run();
                activated = true;
            }

            if (getProgress() >= 1f) {
                onCompletionListener.run();
                completed = true;
            }
        }
    }

    abstract void update();

    public boolean isCompleted() {
        return completed;
    }

    public void setOnActivationListener(final Runnable onActivationListener) {
        this.onActivationListener = onActivationListener;
    }

    public void setOnCompletionListener(final Runnable onCompletionListener) {
        this.onCompletionListener = onCompletionListener;
    }

    public float getProgress() {
        return (float) ticks / maxTicks;
    }

    public abstract double getActivationProgress();
}
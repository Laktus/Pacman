package de.laktus.pacman;

import java.awt.*;

public class FadeInTransition extends Transition {
    public FadeInTransition(int maxTicks) {
        super(maxTicks);
    }

    @Override
    public void draw(Graphics2D g, Dimension panelSize) {
        final float progress = getProgress();
        final Color oldColor = g.getColor();
        final Color fadeInColor = new Color(0f, 0f, 0f, (float) (Math.sin(progress * Math.PI)));
        g.setColor(fadeInColor);
        g.fillRect(0, 0, panelSize.width, panelSize.height);
        g.setColor(oldColor);
    }

    @Override
    public void update() {
    }

    @Override
    public double getActivationProgress() {
        return 0.5;
    }
}
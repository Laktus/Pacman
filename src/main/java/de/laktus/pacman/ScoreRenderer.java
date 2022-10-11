package de.laktus.pacman;

import java.awt.*;
import java.util.function.Supplier;

public class ScoreRenderer implements Renderer {
    private final Supplier<Integer> scoreSupplier;
    private final Supplier<Integer> highScoreSupplier;

    public ScoreRenderer(final Supplier<Integer> scoreSupplier,
                         final Supplier<Integer> highScoreSupplier) {
        this.scoreSupplier = scoreSupplier;
        this.highScoreSupplier = highScoreSupplier;
    }

    @Override
    public void draw(final Dimension screenSize, final Graphics2D g) {
        Font oldFont = g.getFont();
        g.setFont(new Font("Emulogic", 0, (int) (screenSize.height * 0.02)));
        FontMetrics fontMetrics = g.getFontMetrics();
        int sw = fontMetrics.stringWidth("1UP");
        int sh = fontMetrics.getAscent() + fontMetrics.getDescent();
        int sYOffset = (int) (0.02 * screenSize.height);
        int sXOffset = (int) (0.02 * screenSize.width);

        g.setColor(Color.WHITE);
        g.drawString("1UP", sXOffset, sYOffset + sh);
        int score = scoreSupplier.get();
        int scoreYOffset = sYOffset + sh;
        int scoreXOffset = sXOffset + sw / 2;
        g.drawString(String.format("%02d", score), scoreXOffset, scoreYOffset + sh);


        int hw = fontMetrics.stringWidth("High Score");
        int hx = (screenSize.width - hw) / 2;
        int hy = sYOffset;
        g.drawString("High Score", hx, hy + sh);
        int highscore = highScoreSupplier.get();
        g.drawString(String.format("%02d", highscore), hx + hw / 2, scoreYOffset + sh);

        g.setFont(oldFont);
    }
}
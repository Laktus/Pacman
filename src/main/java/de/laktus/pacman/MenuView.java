package de.laktus.pacman;

import java.awt.*;
import java.awt.geom.Path2D;
import java.util.function.Supplier;

public class MenuView implements View {
    private static final String TITLE_STRING = "PAC-MAN";
    private final Supplier<Integer> selection;
    private final String[] buttonStrings;

    public MenuView(Supplier<Integer> selection, String[] buttonStrings) {
        this.selection = selection;
        this.buttonStrings = buttonStrings;
    }


    @Override
    public void draw(final Dimension screenSize, final Graphics2D g) {
        g.setColor(Color.decode("#FFBF00"));
        g.fillRect(0, 0, screenSize.width, screenSize.height);

        g.setColor(Color.BLACK);
        int pw = (int) (screenSize.width * 0.9d);
        int ph = (int) (screenSize.height * 0.9d);
        int px = (screenSize.width - pw) / 2;
        int py = (screenSize.height - ph) / 2;
        g.drawRect(px, py, pw, ph);

        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        Font oldFont = g.getFont();
        g.setFont(new Font("PacFont", 0, (int)(screenSize.height * 0.125)));
        FontMetrics fontMetrics = g.getFontMetrics();
        int sw = fontMetrics.stringWidth(TITLE_STRING);
        int sh = fontMetrics.getAscent() + fontMetrics.getDescent();
        int sYOffset = (int) (0.2 * screenSize.height);
        int sx = px + (pw - sw) / 2;
        int sy = py + ph / 2 + sh;

        g.drawString(TITLE_STRING, sx, sy - sYOffset);

        g.setFont(new Font("PacFont", 0, 30));
        fontMetrics = g.getFontMetrics();
        for (int i = 1; i <= 2; i++) {
            g.setColor(Color.BLACK);
            int startW = (int) (screenSize.width * 0.22d);
            int startH = (int) (screenSize.height * 0.1d);
            int startYOffset = (int) (0.0125 * screenSize.height);
            int startX = (screenSize.width - startW) / 2;
            int startY = (screenSize.height - startH) / 2;

            if (i - 1 == selection.get()) {
                double startXOffset = 0.02 * screenSize.width;
                Path2D path2D = new Path2D.Double();
                path2D.moveTo(startX + startXOffset, startY + 0.375*startH + i * startH + (i-1) * startYOffset);
                path2D.lineTo(startX + startXOffset, startY + 0.625*startH + i * startH + (i-1) * startYOffset);
                path2D.lineTo(startX + startW* 0.075 + startXOffset, startY + 1/2d*startH + i * startH + (i-1) * startYOffset);
                path2D.lineTo(startX + startXOffset, startY + 0.375*startH + i * startH + (i-1) * startYOffset);
                g.fill(path2D.createTransformedShape(null));
            }

            //g.drawRect(startX, startY + i * (startH) + (i - 1) * startYOffset, startW, startH);
            sh = fontMetrics.getAscent();

            sw = fontMetrics.stringWidth(buttonStrings[i - 1]);
            g.drawString(buttonStrings[i - 1], startX + (startW - sw) / 2, startY + i * (startH) + (i - 1) * startYOffset + (sh + startH) / 2);
        }

        g.setFont(oldFont);
    }
}
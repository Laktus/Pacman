package de.laktus.pacman;

import java.awt.*;

public class TileMapRenderer implements Renderer {
    private final Tile[] tileMap;
    private final double tileSize;

    public TileMapRenderer(final Tile[] tileMap, final double tileSize) {
        this.tileMap = tileMap;
        this.tileSize = tileSize;
    }

    @Override
    public void draw(final Dimension screenSize, final Graphics2D g) {
        for (final Tile tile : tileMap) {
            final int tx = (int) (tile.getX() * (tileSize));
            final int ty = (int) (tile.getY() * (tileSize));
            final int ts = (int) tileSize;
            if (tile.isSolid()) {
                g.setColor(Color.BLUE);
                g.fillRect(tx, ty, ts, ts);
            } else {
                g.setColor(Color.BLACK);
                g.drawRect(tx, ty, ts, ts);
            }

            if (tile.hasPellet()) {
                g.setColor(Color.WHITE);
                double width = tileSize / 4;
                double height = tileSize / 4;
                int px = (int) (tile.getX() * (tileSize) + tileSize / 2 - width / 2);
                int py = (int) (tile.getY() * (tileSize) + tileSize / 2 - height / 2);
                g.fillOval(px, py, (int) width, (int) height);
            }
        }
    }
}
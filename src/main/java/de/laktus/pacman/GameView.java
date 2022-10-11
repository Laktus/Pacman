package de.laktus.pacman;

import java.awt.*;
import java.util.List;

public class GameView implements View {
    private final TileMapRenderer tileRenderer;
    private final PlayerRenderer playerRenderer;
    private final List<GhostRenderer> ghostRenderers;
    private final ScoreRenderer scoreRenderer;
    private final double mapWidth;
    private final double mapHeight;

    public GameView(final ScoreRenderer scoreRenderer,
                    final TileMapRenderer tileRenderer,
                    final PlayerRenderer playerRenderer,
                    final List<GhostRenderer> ghostRenderers,
                    final double mapWidth,
                    final double mapHeight) {
        this.tileRenderer = tileRenderer;
        this.playerRenderer = playerRenderer;
        this.ghostRenderers = ghostRenderers;
        this.scoreRenderer = scoreRenderer;
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
    }


    @Override
    public void draw(final Dimension screenSize, final Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, screenSize.width, screenSize.height);

        final double translateX = (screenSize.getWidth() - mapWidth) / 2;
        final double translateY = (screenSize.getHeight() - mapHeight) / 2;
        g.translate(translateX, translateY);

        tileRenderer.draw(screenSize, g);
        playerRenderer.draw(screenSize, g);
        ghostRenderers.forEach(ghostRenderer -> ghostRenderer.draw(screenSize, g));

        g.translate(-translateX, -translateY);

        scoreRenderer.draw(screenSize, g);
    }
}
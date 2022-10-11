package de.laktus.pacman;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GhostRenderer implements Renderer {
    private final Ghost ghost;
    private final Map<Direction, List<BufferedImage>> ghostFramesByDirection;
    private final double tileSize;

    public GhostRenderer(final Ghost ghost, final double tileSize) {
        this.ghost = ghost;
        this.ghostFramesByDirection = new HashMap<>();
        this.readGhostSheet();
        this.tileSize = tileSize;
    }

    @Override
    public void draw(final Dimension screenSize, final Graphics2D g) {
        final Image image = ghostFramesByDirection.get(ghost.getDirection()).get((ghost.getTicks() / 10) % 2);
        g.drawImage(image.getScaledInstance((int) tileSize, (int) tileSize, Image.SCALE_DEFAULT), (int) (ghost.getX() * (tileSize)), (int) (ghost.getY() * (tileSize)), null);

        if (Main.DEBUG) {
            g.setColor(Color.GREEN);
            var rectSize = 0.8d;
            var rayCast = (1d - rectSize);
            var rectOffset = rayCast / 2;
            final Direction direction = ghost.getDirection();
            final Rectangle2D.Double rect = new Rectangle2D.Double(
                    ghost.getX() + rectOffset + direction.dx * rayCast,
                    ghost.getY() + rectOffset + direction.dy * rayCast,
                    rectSize,
                    rectSize
            );
            g.drawRect((int) (rect.getX() * (tileSize)), (int) (rect.getY() * (tileSize)), (int) (tileSize * rectSize), (int) (tileSize * rectSize));
        }
    }

    private void readGhostSheet() {
        try {
            final BufferedImage pacmanSheet = ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResource("ghosts.png")));
            final Direction[] directions = Direction.values();
            for (Direction direction : directions) {
                for (int i = 0; i < 2; i++) {
                    final int startX = direction.ordinal() * 2 + i;
                    final BufferedImage pacmanFrames = pacmanSheet.getSubimage(startX * 16, ghost.getGhostType() * 16, 16, 16);
                    ghostFramesByDirection.putIfAbsent(direction, new ArrayList<>());
                    ghostFramesByDirection.get(direction).add(pacmanFrames);
                }
            }
        } catch (IOException e) {
            //TODO: Add error mechanism
            e.printStackTrace();
        }
    }
}
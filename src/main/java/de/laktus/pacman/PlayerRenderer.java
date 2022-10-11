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

public class PlayerRenderer implements Renderer {
    private final Player player;
    private final Map<Direction, List<BufferedImage>> pacmanFramesByDirection;
    private final double tileSize;

    public PlayerRenderer(final Player player, final double tileSize) {
        this.player = player;
        this.pacmanFramesByDirection = new HashMap<>();
        this.tileSize = tileSize;
        this.readPacmanSheet();
    }

    private void readPacmanSheet() {
        try {
            final BufferedImage pacmanSheet = ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResource("pacman.png")));
            for (Direction direction : Direction.values()) {
                for (int i = 0; i < 2; i++) {
                    final BufferedImage pacmanFrames = pacmanSheet.getSubimage(i * 15, direction.ordinal() * 15, 15 - i, 15);
                    pacmanFramesByDirection.putIfAbsent(direction, new ArrayList<>());
                    pacmanFramesByDirection.get(direction).add(pacmanFrames);
                }
            }
        } catch (IOException e) {
            //TODO: Add error mechanism
            e.printStackTrace();
        }
    }

    @Override
    public void draw(final Dimension screenSize, final Graphics2D g) {
        g.setColor(Color.YELLOW);

        final Image image = pacmanFramesByDirection.get(player.getDirection()).get((player.getTicks() / 10) % 2);
        g.drawImage(image.getScaledInstance((int) tileSize, (int) tileSize, Image.SCALE_DEFAULT), (int) (player.getX() * (tileSize)), (int) (player.getY() * (tileSize)), null);

        if (Main.DEBUG) {
            g.setColor(Color.GREEN);
            var rectSize = 0.8d;
            var rayCast = (1d - rectSize);
            var rectOffset = rayCast / 2;
            final Direction direction = player.getDirection();
            final Rectangle2D.Double rect = new Rectangle2D.Double(
                    player.getX() + rectOffset + direction.dx * rayCast,
                    player.getY() + rectOffset + direction.dy * rayCast,
                    rectSize,
                    rectSize
            );
            g.drawRect((int) (rect.getX() * (tileSize)), (int) (rect.getY() * (tileSize)), (int) (tileSize * rectSize), (int) (tileSize * rectSize));
        }
    }
}
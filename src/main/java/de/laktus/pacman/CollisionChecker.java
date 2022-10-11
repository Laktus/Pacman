package de.laktus.pacman;

import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class CollisionChecker {
    private final Player player;
    private final TileManager tileManager;
    private final List<Ghost> ghosts;

    public CollisionChecker(final Player player, final List<Ghost> ghosts, final TileManager tileManager) {
        this.player = player;
        this.ghosts = ghosts;
        this.tileManager = tileManager;
    }

    public Tile checkWillHitTileWithCondition(double ex, double ey, double dx, double dy, Predicate<Tile> condition) {
        var rectSize = 0.8d;
        var rayCast = (1d - rectSize);
        var rectOffset = rayCast / 2;
        final Rectangle2D.Double entityRect = new Rectangle2D.Double(
                ex + dx * rayCast + rectOffset,
                ey + dy * rayCast + rectOffset,
                rectSize,
                rectSize
        );
        for (final Tile tile : tileManager.getTiles()) {
            final Rectangle2D.Double tileRect = new Rectangle2D.Double(tile.getX(), tile.getY(), 1d, 1d);
            if (entityRect.intersects(tileRect) && condition.test(tile)) {
                return tile;
            }
        }
        return null;
    }

    public void checkWallCollision() {
        final Direction direction = player.getDirection();

        if (direction == null) {
            return;
        }

        player.setCollision(checkWillHitTileWithCondition(player.getX(), player.getY(), direction.dx, direction.dy, Tile::isSolid));

        ghosts.forEach(ghost -> {
            final Direction ghostDirection = ghost.getDirection();

            if (ghostDirection == null) {
                return;
            }

            ghost.setCollision(checkWillHitTileWithCondition(ghost.getX(), ghost.getY(), ghostDirection.dx, ghostDirection.dy, Tile::isSolid));
        });
    }

    public void checkPellets(Consumer<Integer> scoreFunction) {
        Tile pelletTile = checkWillHitTileWithCondition(player.getX(), player.getY(), 0, 0, Tile::hasPellet);

        if (pelletTile != null) {
            pelletTile.eatPellet();
            scoreFunction.accept(10);
        }
    }

    public void checkBoundaries() {
        int width = tileManager.getWidth();
        int height = tileManager.getHeight();
        if (player.getX() >= width - 1) {
            player.setX(0);
        } else if (player.getX() < 0) {
            player.setX(width - 1);
        }

        if (player.getY() >= height - 1) {
            player.setY(0);
        } else if (player.getY() < 0) {
            player.setY(height - 1);
        }

        ghosts.forEach(ghost -> {
            if (ghost.getX() >= width - 1) {
                ghost.setX(0);
            } else if (ghost.getX() < 0) {
                ghost.setX(width - 1);
            }

            if (ghost.getY() >= height - 1) {
                ghost.setY(0);
            } else if (ghost.getY() < 0) {
                ghost.setY(height - 1);
            }
        });
    }
}
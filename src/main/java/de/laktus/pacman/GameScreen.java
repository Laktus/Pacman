package de.laktus.pacman;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.stream.Collectors;

import static java.awt.event.KeyEvent.*;

public class GameScreen extends Screen {
    private final static double TILE_MAP_SCALE_FACTOR = 0.8;
    private GameView gameView;
    private final CollisionChecker collisionChecker;
    private final Player player;
    private final List<Ghost> ghosts;
    private final KeyAdapter keyAdapter;
    private final TileManager tileManager;
    private double tileSize;
    private int score;
    private int lifes;

    public GameScreen() {
        this.keyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                Direction direction = null;
                switch (e.getKeyCode()) {
                    case VK_RIGHT:
                        direction = Direction.RIGHT;
                        break;
                    case VK_LEFT:
                        direction = Direction.LEFT;
                        break;
                    case VK_UP:
                        direction = Direction.UP;
                        break;
                    case VK_DOWN:
                        direction = Direction.DOWN;
                        break;
                }

                if (direction != null) {
                    player.setDirection(direction);
                }
            }
        };
        this.tileManager = new TileManager("tiles.txt");

        this.player = new Player(tileManager.getPlayerX(), tileManager.getPlayerY());
        this.ghosts = tileManager.getInitialGhostPositions().stream()
                              .map(initialPosition -> new Ghost(initialPosition.first, initialPosition.second, player::getX, player::getY))
                              .collect(Collectors.toList());
        this.collisionChecker = new CollisionChecker(player, ghosts, tileManager);
        this.lives = 3;

    }

    @Override
    public void onMounted(final Dimension panelSize) {
        updatePanel(panelSize);
    }

    @Override
    public void onComponentUpdate(final ComponentEvent componentEvent) {
        updatePanel(componentEvent.getComponent().getSize());
    }

    private double calculateTileSize(final int tileWidth,
                                     final int tileHeight,
                                     final double mapWidth,
                                     final double mapHeight) {
        if (mapWidth < mapHeight) {
            return Math.floor(mapWidth / tileWidth);
        } else {
            return Math.floor(mapHeight / tileHeight);
        }
    }

    private void updatePanel(final Dimension panelSize) {
        final double expectedMapWidth = panelSize.getWidth() * TILE_MAP_SCALE_FACTOR;
        final double expectedMapHeight = panelSize.getHeight() * TILE_MAP_SCALE_FACTOR;
        this.tileSize = calculateTileSize(tileManager.getWidth(), tileManager.getHeight(),
                                          expectedMapWidth,
                                          expectedMapHeight);
        final double realMapWidth = tileSize * tileManager.getWidth();
        final double realMapHeight = tileSize * tileManager.getHeight();

        this.gameView = new GameView(
                new ScoreRenderer(() -> score, () -> 0),
                new TileMapRenderer(tileManager.getTiles(), tileSize),
                new PlayerRenderer(player, tileSize),
                this.ghosts.stream()
                        .map(ghost -> new GhostRenderer(ghost, tileSize))
                        .collect(Collectors.toList()),
                realMapWidth,
                realMapHeight
        );
    }

    public void update() {
        if (lifes == 0) {
            screenManager.push(null, new FadeInTransition(60));
        } else {
            collisionChecker.checkBoundaries();
            collisionChecker.checkWallCollision();
            collisionChecker.checkPellets(v -> score = score + v);

            player.update();

            ghosts.forEach(Ghost::update);
        }
    }

    @Override
    public View getView() {
        return this.gameView;
    }

    @Override
    public KeyListener getKeyListener() {
        return keyAdapter;
    }
}
package de.laktus.pacman;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Ghost {
    //TODO: Extract to config file
    private final static int GHOST_TYPES = 4;
    private Supplier<Double> playerXSupplier;
    private Supplier<Double> playerYSupplier;
    private int x;
    private int y;

    private Direction moveDir;
    private Direction nextDir;
    private Tile collision;
    private int ticks;
    private final int ghostType;

    public Ghost(final int x, final int y, Supplier<Double> playerXSupplier, Supplier<Double> playerYSupplier) {
        this.playerXSupplier = playerXSupplier;
        this.playerYSupplier = playerYSupplier;
        this.x = x;
        this.y = y;
        this.moveDir = this.nextDir = determineNextDir(playerXSupplier.get(), playerYSupplier.get(), false);

        final Random random = ThreadLocalRandom.current();
        this.ghostType = random.nextInt(GHOST_TYPES);
    }

    public void update() {
        if (collision == null || ticks != 0) {
            ticks++;
            if (ticks >= 20) {
                x += moveDir.dx;
                y += moveDir.dy;
                ticks = 0;
                moveDir = nextDir;
                nextDir = determineNextDir(playerXSupplier.get(), playerYSupplier.get(), false);
            }
        } else {
            moveDir = nextDir = determineNextDir(playerXSupplier.get(), playerYSupplier.get(), true);
        }
    }

    public Direction determineNextDir(final double playerX, final double playerY, final boolean ignore) {
        final Direction[] values = Direction.values();
        if (ignore) {
            final List<Direction> possibleDirs = Arrays.stream(values).filter(direction -> !direction.equals(moveDir)).collect(Collectors.toList());
            return possibleDirs.get((int) (possibleDirs.size() * Math.random()));
        } else {
            return values[(int) (Math.random() * values.length)];
        }
    }

    public void setCollision(Tile collision) {
        this.collision = collision;
    }

    public void setDirection(Direction moveDir) {
        if (collision == null) {
            if (this.moveDir.opposite().equals(moveDir)) {
                x += this.moveDir.dx;
                y += this.moveDir.dy;
                ticks = 20 - ticks;
                this.moveDir = moveDir;
                this.nextDir = moveDir;
            } else {
                this.nextDir = moveDir;
            }
        } else {
            if (ticks == 0) {
                this.moveDir = moveDir;
                this.nextDir = moveDir;
            }
        }
    }

    public Integer getTicks() {
        return ticks;
    }

    public Direction getDirection() {
        return moveDir;
    }

    public void setX(final int x){
        this.x = x;
    }

    public void setY(final int y){
        this.y = y;
    }

    public double getX() {
        return x + moveDir.dx * ticks / 20d;
    }

    public double getY() {
        return y + moveDir.dy * ticks / 20d;
    }

    public int getGhostType() {
        return ghostType;
    }
}
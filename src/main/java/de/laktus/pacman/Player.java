package de.laktus.pacman;

public class Player {
    private int x;
    private int y;

    private Direction moveDir;
    private Direction nextDir;
    private Tile collision;
    private int ticks;

    public Player(final int x, final int y) {
        this.x = x;
        this.y = y;

        this.moveDir = this.nextDir = Direction.RIGHT;

    }

    public void update() {
        if (collision == null || ticks != 0) {
            ticks++;
            if (ticks >= 20) {
                x += moveDir.dx;
                y += moveDir.dy;
                ticks = 0;
                moveDir = nextDir;
            }
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

    public void setX(final int x) {
        this.x = x;
    }

    public void setY(final int y) {
        this.y = y;
    }

    public double getX() {
        return x + moveDir.dx * ticks / 20d;
    }

    public double getY() {
        return y + moveDir.dy * ticks / 20d;
    }
}
package de.laktus.pacman;

public class Tile {
    private final int x;
    private final int y;
    private final boolean solid;
    private boolean pellet;

    public Tile(final int x, final int y, final boolean solid, final boolean pellet) {
        this.x = x;
        this.y = y;
        this.solid = solid;
        this.pellet = pellet;
    }

    public boolean isSolid() {
        return solid;
    }

    public boolean hasPellet() {
        return pellet;
    }

    public void eatPellet() {
        pellet = false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
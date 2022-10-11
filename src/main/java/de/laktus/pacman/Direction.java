package de.laktus.pacman;

public enum Direction {
    RIGHT(1, 0) {
        @Override
        Direction opposite() {
            return LEFT;
        }
    }, LEFT(-1, 0) {
        @Override
        Direction opposite() {
            return RIGHT;
        }
    }, UP(0, -1) {
        @Override
        Direction opposite() {
            return DOWN;
        }
    }, DOWN(0, 1) {
        @Override
        Direction opposite() {
            return UP;
        }
    };

    public final int dx;
    public final int dy;

    Direction(final int dx, final int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    abstract Direction opposite();
}
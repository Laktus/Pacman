package de.laktus.pacman;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TileManager {
    private String fileName;

    private int width;
    private int height;
    private int playerX;
    private int playerY;

    private Tile[] tiles;
    private final List<Pair<Integer, Integer>> initialGhostPositions;

    public TileManager(String fileName) {
        this.fileName = fileName;
        this.initialGhostPositions = new ArrayList<>();
        try {
            processFile();
        } catch (URISyntaxException | IOException e) {
            //TODO: add error mechanism
            e.printStackTrace();
        }
    }

    private void processFile() throws URISyntaxException, IOException {
        final URL url = TileManager.class.getClassLoader().getResource(fileName);

        try (final BufferedReader bufferedReader = Files.newBufferedReader(Path.of(url.toURI()))) {
            final String[] dimensions = bufferedReader.readLine().split(" ");
            this.width = Integer.parseInt(dimensions[0]);
            this.height = Integer.parseInt(dimensions[1]);

            final Tile[] tiles = new Tile[width * height];
            String line;
            int index = 0;
            while ((line = bufferedReader.readLine()) != null) {
                final char[] tileDescriptors = line.toCharArray();
                for (final char tileDescriptor : tileDescriptors) {
                    int x = index % width;
                    int y = index / width;

                    switch (tileDescriptor) {
                        case '1':
                            tiles[index++] = new Tile(x, y, true, false);
                            break;
                        case '0':
                            tiles[index++] = new Tile(x, y, false, true);
                            break;
                        case 'N':
                            tiles[index++] = new Tile(x, y, false, false);
                            break;
                        case 'P':
                            playerX = x;
                            playerY = y;
                            tiles[index++] = new Tile(x, y, false, false);
                            break;
                        case 'G':
                            initialGhostPositions.add(new Pair<>(x, y));
                            tiles[index++] = new Tile(x, y, false, false);
                            break;
                    }
                }
            }

            this.tiles = new Tile[tiles.length];
            System.arraycopy(tiles, 0, this.tiles, 0, tiles.length);
        }
    }

    public List<Pair<Integer, Integer>> getInitialGhostPositions() {
        return initialGhostPositions;
    }

    public Tile[] getTiles() {
        return tiles;
    }

    public int getPlayerX() {
        return playerX;
    }

    public int getPlayerY() {
        return playerY;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
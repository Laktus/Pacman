package de.laktus.pacman;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MenuScreen extends Screen {
    private final MenuView menuView;
    private int selection;
    private static final String[] BUTTON_STRINGS = {"START", "END"};

    private final KeyAdapter keyAdapter;

    public MenuScreen() {
        this.keyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_DOWN:
                        selection = (selection + 1) % BUTTON_STRINGS.length;
                        break;
                    case KeyEvent.VK_UP:
                        selection = ((selection - 1) + BUTTON_STRINGS.length) % BUTTON_STRINGS.length;
                        break;
                    case KeyEvent.VK_ENTER:
                        handleCommand(selection);
                        break;
                }

            }
        };
        this.menuView = new MenuView(() -> selection, BUTTON_STRINGS);
    }

    private void handleCommand(int selection) {
        switch (selection) {
            case 0:
                screenManager.push(new GameScreen(), new FadeInTransition(60));
                break;
            case 1:
                System.exit(0);
                break;
        }
    }

    @Override
    public void update() {
    }

    @Override
    protected View getView() {
        return menuView;
    }

    @Override
    public KeyListener getKeyListener() {
        return keyAdapter;
    }
}
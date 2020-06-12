import bagel.*;
import bagel.map.TiledMap;
import bagel.util.Point;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class ShadowDefend extends AbstractGame {
    /**
     * Change the constant FPS to change the frame-per-second to suitable value
     */
    private static final double FPS = 60;
    private static final Keys START_KEY = Keys.S;
    private static final Keys SPEEDUP_KEY = Keys.L;
    private static final Keys SPEEDDOWN_KEY = Keys.K;
    private static final int TOTAL_LEVELS = 2;
    private static final int WIDTH = 1024;
    private static final int HEIGHT = 768;
    private static final int DEFAULT_MONEY = 500;
    private static final int DEFAULT_LIVES = 25;
    private static double timeScale = 1;
    private static int money = DEFAULT_MONEY;
    private static int lives = DEFAULT_LIVES;
    private static boolean gameWon = false;
    private static boolean placingTower = false;
    private static boolean waveInProgress = false;
    private static double tickCounter = 0;
    private static Level currentLevel;
    private static int currentLevelID = 0;

    public static void main(String[] args) {
        // Create a new instance of game and run
        // TODO: Test the game
        new ShadowDefend().run();
    }

    public ShadowDefend() {
        super(WIDTH, HEIGHT, "ShadowDefend");
        loadNextLevel();
    }

    /**
     * Load the next level. Use this when all waves in a level are finished. By default, it is assumed there is only one
     * waves.txt that contain information about enemies in each wave and the files contain information for each level are
     * in "~/res/levels/" folder and have the format (level_id).tmx.
     */
    public static void loadNextLevel() {
        currentLevelID++;
        money = DEFAULT_MONEY;
        lives = DEFAULT_LIVES;
        // Finished the game
        if (currentLevelID > TOTAL_LEVELS) {
            gameWon = true;
            return;
        }

        String mapSource = "res/levels/" + Integer.toString(currentLevelID) + ".tmx";
        String levelSource = "res/levels/waves.txt";
        currentLevel = new Level(currentLevelID, mapSource, WIDTH, HEIGHT);
        try {
            File waveFile = new File(levelSource);
            Scanner scanner = new Scanner(waveFile);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                currentLevel.addWaveEvent(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Can't find waves file.");
            e.printStackTrace();
        }
    }

    /**
     * Change the player's money
     * @param amount an integer determine the change. Positive integer mean gain money, negative mean loss money
     */
    public static void changeMoney(int amount) {
        //TODO: Check if money earned is correct
        money += amount;
    }

    /**
     * Change the player's lives. If lost all lives, the game end.
     * @param amount an integer determine the change. Positive integer mean gain lives, negative mean loss lives
     */
    public static void changeLives(int amount) {
        lives += amount;
        if (lives <=0) {
            Window.close();
        }
    }

    @Override
    protected void update(Input input) {
        // TODO: The game has a glitch that flash the map if press S after win
        // Check input
        if (input.wasPressed(START_KEY) && !waveInProgress && currentLevel.getEnemyList().size()==0) {
            // Do nothing if the game is won and over
            if (gameWon) {
                return;
            }
            // Spawn a wave if current wave already finished AND no slicers left on the map
            currentLevel.startNextWave();
            waveInProgress = true;
        }
        if (input.wasPressed(SPEEDDOWN_KEY) && (timeScale > 1)) {
            timeScale--;
        }
        if (input.wasPressed(SPEEDUP_KEY)) {
            timeScale++;
        }
        if (input.wasPressed(MouseButtons.LEFT)) {
            BuyPanel.checkClick(money, input.getMousePosition());
        }
        if (input.wasPressed(MouseButtons.RIGHT)) {
            BuyPanel.deSelect();
        }

        // Draw
        currentLevel.update(input);
        StatusPanel.draw(currentLevel.getCurrentWaveID(), timeScale, lives, gameWon, placingTower, waveInProgress);
        BuyPanel.draw(money, input.getMousePosition());

    }

    /* Getters and Setters */
    public static double getTimeScale() {
        return timeScale;
    }

    public static double getFPS() {
        return FPS;
    }

    public static void setWaveInProgress(boolean bool) {
        ShadowDefend.waveInProgress = bool;
    }

    public static Level getCurrentLevel() {
        return currentLevel;
    }

    public static int getWIDTH() {
        return WIDTH;
    }

    public static int getHEIGHT() {
        return HEIGHT;
    }

    public static int getMoney() {
        return money;
    }

    public static void setPlacingTower(boolean placingTower) {
        ShadowDefend.placingTower = placingTower;
    }

    public static boolean isWaveInProgress() {
        return waveInProgress;
    }
}

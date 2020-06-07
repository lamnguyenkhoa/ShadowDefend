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
    private static int timeScale = 1;
    private static int money = 0;
    private static boolean waveInProgress = false;
    private static double tickCounter = 0;
    private static Level currentLevel;
    private static int currentLevelID = 0;

    public static void main(String[] args) {
        // Create a new instance of game and run
        new ShadowDefend().run();
    }

    public ShadowDefend() {
        super(WIDTH, HEIGHT, "ShadowDefend");
        loadNextLevel();
    }

    public static void loadNextLevel() {
        currentLevelID++;
        // Finished the game
        if (currentLevelID > TOTAL_LEVELS) {
            //TODO: Update status to "Winner!"
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

    public static void addMoney(int amount) {
        money += amount;
    }

    @Override
    protected void update(Input input) {
        // Check input
        if (input.wasPressed(START_KEY) && !waveInProgress && currentLevel.getEnemyList().size()==0) {
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

        // Draw
        currentLevel.update(input);

    }

    /* Getters and Setters */
    public static int getTimeScale() {
        return timeScale;
    }

    public static double getFPS() {
        return FPS;
    }
}


//TODO:
// -Improve slicer speed at curve;
// -update according to feedback
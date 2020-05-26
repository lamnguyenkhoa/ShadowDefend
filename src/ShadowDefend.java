import bagel.*;
import bagel.map.TiledMap;
import bagel.util.Point;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ShadowDefend extends AbstractGame {
    /**
     * Change the constant FPS to change the frame-per-second to suitable value
     */
    private static final double FPS = 60;

    private static final int MAX_SLICER_A_WAVE = 5;
    private static final double BASE_SPAWN_INTERVAL = 5;
    private static final Keys START_KEY = Keys.S;
    private static final Keys SPEEDUP_KEY = Keys.L;
    private static final Keys SPEEDDOWN_KEY = Keys.K;
    private static int timeScale = 1;
    private static List<Point> path;
    private static TiledMap map;
    private static Image slicerImg;
    private static List<Slicer> slicerList = new ArrayList<>();
    private static Point spawnPoint;
    private static int nSlicerSpawned = 0;
    private static boolean waveInProgress = false;
    private static double tickCounter = 0;
    private static boolean project1Checker = false;

    public static void main(String[] args) {
        // Create a new instance of game and run
        new ShadowDefend().run();
    }

    public ShadowDefend() {
        // Init for the game
        map = new TiledMap("res/levels/1.tmx");
        slicerImg = new Image("res/images/slicer.png");
        path = map.getAllPolylines().get(0);
        spawnPoint = new Point(path.get(0).x, path.get(0).y);

    }

    @Override
    protected void update(Input input) {
        // Check input
        if (input.wasPressed(START_KEY) && !waveInProgress && slicerList.size()==0) {
            // Spawn a wave if current wave already finished AND no slicers left on the map
            tickCounter = FPS * BASE_SPAWN_INTERVAL; // the first slicer will spawn immediately
            nSlicerSpawned = 0;
            waveInProgress = true;
            project1Checker = true; // project1 use only
        }
        if (input.wasPressed(SPEEDDOWN_KEY) && (timeScale > 1)) {
            timeScale--;
            //System.out.println("timescale: "+timeScale);
        }
        if (input.wasPressed(SPEEDUP_KEY)) {
            timeScale++;
            //System.out.println("timescale: "+timeScale);
        }

        // Spawning
        if (waveInProgress) {
            tickCounter += timeScale;
            // When enough ticks, spawn a new slicer
            if (tickCounter >= (BASE_SPAWN_INTERVAL * FPS)) {
                slicerList.add(new Slicer(0, spawnPoint, new DrawOptions()));
                nSlicerSpawned++;
                tickCounter = 0;
                if (nSlicerSpawned == MAX_SLICER_A_WAVE)
                    waveInProgress = false;
            }
        }

        // Calculation pos and rotate for each slicer
        Iterator<Slicer> slicerIterator = slicerList.iterator();
        while (slicerIterator.hasNext()) {
            Slicer slicer = slicerIterator.next();
            slicer.calculateRotation();
            slicer.calculatePosition();
            if (slicer.isFinished())
                slicerIterator.remove();
        }

        // Draw
        map.draw(0, 0, 0, 0, Window.getWidth(), Window.getHeight());
        for(Slicer slicer : slicerList) {
            slicerImg.draw(slicer.getPosition().x, slicer.getPosition().y, slicer.getDrawOptions());
        }

        // Project 1 check: If one wave spawned and all slicers finished traverse, end the game
        if (project1Checker && (slicerList.size() == 0))
            System.exit(0);
    }

    /* Getters and Setters */
    public static List<Point> getPath() {
        return path;
    }

    public static int getTimeScale() {
        return timeScale;
    }
}


//TODO:
// -Improve slicer speed at curve;
// -Remove project1 checker when move to project2
// -update according to feedback
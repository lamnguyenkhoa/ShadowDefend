import bagel.Input;
import bagel.Window;
import bagel.map.TiledMap;
import bagel.util.Point;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Level {
    private int id;
    private TiledMap tiledMap;
    private List<Point> path;
    private Point spawnPoint;
    private boolean levelFinished = false;
    private List<Tower> towerList = new ArrayList<>();
    private List<Enemy> enemyList = new ArrayList<>();
    private List<Enemy> queueEnemyList = new ArrayList<>();
    private List<Projectile> projectileList = new ArrayList<>();
    private List<WaveEvent> waveEventList = new ArrayList<>();
    private final int WIDTH;
    private final int HEIGHT;
    private int tickCounter = 0;
    private int currentWaveID = 0;
    private WaveEvent currentWaveEvent;
    private final double FPS = ShadowDefend.getFPS();
    private boolean startedWave = false;
    private boolean availableWaveEvent = false;

    /**
     * Create a new instance of Level. This is the play-field of the game where enemies moving, towers are placed and
     * bullets flying.
     * @param id        an integer used to identify the map
     * @param mapSource the location that contain the map file .tmx
     * @param width     the width of the game screen (in pixel)
     * @param height    the height of the game screen (in pixel)
     */
    public Level(int id, String mapSource, int width, int height) {
        // source format: "res/levels/1.tmx"
        this.id = id;
        this.tiledMap = new TiledMap(mapSource);
        this.path = tiledMap.getAllPolylines().get(0);
        this.spawnPoint = new Point(path.get(0).x, path.get(0).y);
        this.WIDTH = width;
        this.HEIGHT = height;
    }

    public void update(Input input) {
        /*
        Check if the current level is finished yet
        Check and spawn enemies that are from the wave if conditions met
        If finished current wave event, delete it and start the next wave event
        If finished current wave, signal to ShadowDefend and wait to press S
        Draw the map
        Update position and draw each enemy, projectile, tower
         */

        if (levelFinished && !ShadowDefend.isWaveInProgress()) {
            ShadowDefend.loadNextLevel();
        }

        if (availableWaveEvent) {
            tickCounter += ShadowDefend.getTimeScale();
            if (currentWaveEvent.getEventType().equals("delay")) {
                // Type just delay time
                if ((tickCounter / FPS) * 1000 >= currentWaveEvent.getDelay()) {
                    tickCounter = 0;
                    getNextWaveEvent();
                }
            } else {
                // Type spawn enemy
                if ((tickCounter / FPS) * 1000 >= currentWaveEvent.getDelay()) {
                    if (currentWaveEvent.getQuantity() > 0) {
                        tickCounter = 0;
                        spawnEnemy(currentWaveEvent.getEnemyType(), 0, spawnPoint, path, false);
                        // After finished spawn last enemy, immediately get to next wave event
                        if (currentWaveEvent.getQuantity() == 0) {
                            getNextWaveEvent();
                        }
                    }
                }
            }
        }
        // The wave is completely finished
        if (!availableWaveEvent && startedWave && enemyList.size() == 0) {
            ShadowDefend.setWaveInProgress(false);
            startedWave = false;
            ShadowDefend.changeMoney(150 + currentWaveID*100);
        }
        spawnQueueEnemy();
        updateLevel();
        cleanUp();
    }

    /**
     * Spawn an enemy in the map. If enemy spawned by killing other higher tier enemy, use the spawnQueueEnemy() instead.
     * @param enemyType        string name of the type of enemy
     * @param currentPathPoint integer represent the current point on the path
     * @param spawnPoint       x and y-coordinate of where to spawn enemy on the map (in pixel)
     * @param path             a list of point that determine the path that this instance will move
     * @param fromDeathEvent   information about whether this enemy spawned by killing another enemy
     */
    public void spawnEnemy(String enemyType, int currentPathPoint, Point spawnPoint, List<Point> path, boolean fromDeathEvent) {
        Enemy enemy;
        switch (enemyType) {
            case "slicer":
                enemy = new RegularSlicer(currentPathPoint, spawnPoint, path);
                break;
            case "superslicer":
                enemy = new SuperSlicer(currentPathPoint, spawnPoint, path);
                break;
            case "megaslicer":
                enemy = new MegaSlicer(currentPathPoint, spawnPoint, path);
                break;
            case "apexslicer":
                enemy = new ApexSlicer(currentPathPoint, spawnPoint, path);
                break;
            default:
                // Error
                enemy = null;
                break;
        }
        enemyList.add(enemy);
        if (!fromDeathEvent) {
            currentWaveEvent.reduceQuantity(1);
        }
    }

    /**
     * Use this option to add enemies which are create by the death higher tier enemies.
     */
    public void spawnQueueEnemy() {
        for (Enemy enemy : queueEnemyList) {
            spawnEnemy(enemy.getName(), enemy.getCurrentPathPoint(), enemy.getPosition(), enemy.getPath(), true);
        }
        queueEnemyList.clear();
    }

    /**
     * Call the update() function of these objects: Enemy, Tower and Projectile
     */
    public void updateLevel() {
        tiledMap.draw(0, 0, 0, 0, WIDTH, HEIGHT);
        for (Enemy enemy : enemyList) {
            enemy.update();
        }
        for (Tower tower : towerList) {
            tower.update();
        }
        for (Projectile projectile : projectileList) {
            projectile.update();
        }
    }

    /**
     * Run this after the previous wave event is finished
     */
    public void getNextWaveEvent() {
        /*
        This start each wave event (each line in the waves.txt)
         */
        // Check if there is any waves left. If not, level is finished
        if (waveEventList.size() == 0) {
            levelFinished = true;
            availableWaveEvent = false;
            return;
        }
        // Check if the next wave event has the same id,
        // then delete the old wave event
        if (waveEventList.get(0).getId() == currentWaveID) {
            currentWaveEvent = waveEventList.get(0);
            waveEventList.remove(0);
            // Immediately spawn one enemy
            if (currentWaveEvent.getEventType().equals("spawn")) {
                spawnEnemy(currentWaveEvent.getEnemyType(), 0, spawnPoint, path, false);
            }
        } else {
            // finished all wave events in current wave
            availableWaveEvent = false;
        }
    }

    /**
     * Run this after a wave has finished and the START_WAVE key was pressed.
     */
    public void startNextWave() {
        currentWaveID++;
        availableWaveEvent = true;
        getNextWaveEvent();
        startedWave = true;
        tickCounter = 0;
    }

    /**
     * Function used to read information from a string to create new WaveEvent object that store information about each
     * wave vent.
     * @param line string that contain necessary information
     */
    public void addWaveEvent(String line) {
        WaveEvent newWaveEvent;

        String[] data = line.split(",");
        if (data.length == 3) {
            // delay event type
            newWaveEvent = new WaveEvent(Integer.parseInt(data[0]), data[1], Integer.parseInt(data[2]));
        } else {
            // spawn event type
            newWaveEvent = new WaveEvent(Integer.parseInt(data[0]), data[1], Integer.parseInt(data[2]),
                    data[3], Integer.parseInt(data[4]));
        }
        waveEventList.add(newWaveEvent);
    }

    public void addSplitEnemy(Enemy enemy) {
        queueEnemyList.add(enemy);
    }

    public void addProjectile(Projectile projectile) {
        projectileList.add(projectile);
    }

    public void addTower(Tower tower) {
        towerList.add(tower);
    }

    public List<Point> getPath() {
        return path;
    }

    public List<Enemy> getEnemyList() {
        return enemyList;
    }

    public List<Tower> getTowerList() {
        return towerList;
    }

    public List<Projectile> getProjectileList() {
        return projectileList;
    }

    public int getCurrentWaveID() {
        return currentWaveID;
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    public void cleanUp() {
        enemyList.removeIf(Enemy::isFinished);
        projectileList.removeIf(Projectile::isFinished);
        towerList.removeIf(Tower::isFinished);
    }
}
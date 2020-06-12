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
        Check and spawn enemy if condition met
        If finished current wave event, delete it and start the next
        If finished current wave, signal to ShadowDefend and wait press S
        Draw the map
        Update position and draw for each enemy, projectile, tower
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

    public void spawnQueueEnemy() {
        for (Enemy enemy : queueEnemyList) {
            spawnEnemy(enemy.getName(), enemy.getCurrentPathPoint(), enemy.getPosition(), enemy.getPath(), true);
        }
        queueEnemyList.clear();
    }

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

    public void getNextWaveEvent() {
        /*
        This start each wave event (each line in the waves.txt)
         */
        // Check if there is any waves left
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

    public void startNextWave() {
        currentWaveID++;
        availableWaveEvent = true;
        getNextWaveEvent();
        startedWave = true;
        tickCounter = 0;
    }

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
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
    private List<Projectile> projectileList = new ArrayList<>();
    private List<WaveEvent> waveEventList = new ArrayList<>();
    private final int WIDTH;
    private final int HEIGHT;
    private int tickCounter = 0;
    private int currentWaveID = 0;
    private WaveEvent currentWaveEvent;
    private final double FPS = ShadowDefend.getFPS();
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
        Draw the map
        Update position and draw for each enemy
        Update position and draw for each projectiles
        Update position and draw for each tower
        Check and spawn enemy if condition met
        If finished current wave event, delete it and start the next
         */

        if (levelFinished) {
            ShadowDefend.loadNextLevel();
        }

        drawLevel();

        /*
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
                        spawnEnemy(currentWaveEvent.getEnemyType());
                    } else {
                        getNextWaveEvent();
                    }
                }
            }
        }
         */
    }

    public void spawnEnemy(String enemyType) {
        //TODO: add 3 other types
        if (enemyType.equals("slicer")) {
            Enemy enemy = new RegularSlicer(0, spawnPoint, path);
        }
        currentWaveEvent.reduceQuantity(1);
    }

    public void drawLevel() {
        tiledMap.draw(0, 0, 0, 0, WIDTH, HEIGHT);
    }

    public void getNextWaveEvent() {
        /*
        This start each wave event (each line in the waves.txt)
         */
        // Check if there is any waves left
        if (waveEventList.size() == 1) {
            levelFinished = true;
            return;
        }
        // Check if the next wave event has the same id,
        // then delete the old wave event
        if (waveEventList.get(1).getId() == currentWaveID) {
            currentWaveEvent = waveEventList.get(1);
            waveEventList.remove(0);
            // Immediately spawn one
            if (currentWaveEvent.getEventType().equals("spawn")) {
                spawnEnemy(currentWaveEvent.getEnemyType());
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

    public List<Point> getPath() {
        return path;
    }

    public List<Enemy> getEnemyList() {
        return enemyList;
    }

    public void deleteFinishedWaveEvent(int id) {
        //TODO: test the Collection.removeIf()
        Iterator<WaveEvent> waveEventIterator = waveEventList.iterator();
        while (waveEventIterator.hasNext()) {
            WaveEvent waveEvent = waveEventIterator.next();
            if (waveEvent.getId() == id)
                waveEventIterator.remove();
        }
    }
}
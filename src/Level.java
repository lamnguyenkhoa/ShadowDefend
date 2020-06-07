import bagel.Input;
import bagel.Window;
import bagel.map.TiledMap;
import bagel.util.Point;

import java.util.Iterator;
import java.util.List;

public class Level {
    private int id;
    private TiledMap tiledMap;
    private List<Point> path;
    private Point spawnPoint;
    private boolean levelFinished = false;
    private List<Tower> towerList;
    private List<Enemy> enemyList;
    private List<Projectile> projectileList;
    private List<WaveEvent> waveEventList;
    private final int WIDTH;
    private final int HEIGHT;
    private int tickCounter = 0;
    private int currentWaveID = 0;

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
        Draw the map
        Update position for each enemy
        Update position for each projectiles
        Update position for each tower
        Check and spawn enemy if condition met
         */
        drawMap();

    }

    public void spawnEnemy(String enemyType) {
        //TODO: add 3 other types
        if (enemyType.equals("slicer")) {
            Enemy enemy = new RegularSlicer(0, spawnPoint, path);
        }
    }

    public void drawMap() {
        tiledMap.draw(0, 0, 0, 0, WIDTH, HEIGHT);
    }

    public void startWaveEvent() {
        // this start each wave event (each line in the waves.txt)

    }

    public void startWave() {
        // this start entire wave = play every waves with current wave id until that id run out
        currentWaveID++;
    }

    public void deleteFinishedWaveEvent(int id) {
        Iterator<WaveEvent> waveEventIterator = waveEventList.iterator();
        while (waveEventIterator.hasNext()) {
            WaveEvent waveEvent = waveEventIterator.next();
            if (waveEvent.getId() == id)
                waveEventIterator.remove();
        }
    }

    public void addWaveEvent(String line) {
        String[] data = line.split(",");
        if (data.length == 3) {
            // delay event type
            waveEventList.add(new WaveEvent(Integer.parseInt(data[0]), data[1], Integer.parseInt(data[2])));
        } else {
            // spawn event type
            waveEventList.add(new WaveEvent(Integer.parseInt(data[0]), data[1], Integer.parseInt(data[2]),
                    data[3], Integer.parseInt(data[4])));
        }
    }

    public List<Point> getPath() {
        return path;
    }

    public List<Enemy> getEnemyList() {
        return enemyList;
    }
}
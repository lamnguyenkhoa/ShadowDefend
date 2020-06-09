import bagel.Image;
import bagel.util.Point;

import java.util.List;

public class SuperSlicer extends Enemy {
    private static final double SPEED = 1.5;
    private static final int HEALTH = 1;
    private static final int PENALTY = 2;
    private static final int REWARD = 15;
    private static final Image IMG = new Image("res/images/superslicer.png");

    public SuperSlicer(int currentPathPoint, Point position, List<Point> path) {
        super(SPEED, HEALTH, PENALTY, REWARD, currentPathPoint, IMG, position, path);
    }

    @Override
    public void deathEvent() {
        int tmpPathPoint = this.getCurrentPathPoint();
        Point tmpPosition = this.getPosition();
        List<Point> tmpPath = this.getPath();
        super.deathEvent();
        ShadowDefend.getCurrentLevel().spawnEnemy("slicer", tmpPathPoint, tmpPosition, tmpPath);
        ShadowDefend.getCurrentLevel().spawnEnemy("slicer", tmpPathPoint, tmpPosition, tmpPath);
    }
}



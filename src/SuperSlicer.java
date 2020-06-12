import bagel.Image;
import bagel.util.Point;

import java.util.List;

public class SuperSlicer extends Enemy {
    private static final String NAME = "superslicer";
    private static final double SPEED = 1.5;
    private static final int HEALTH = 1;
    private static final int PENALTY = 2;
    private static final int REWARD = 15;
    private static final Image IMG = new Image("res/images/superslicer.png");

    /**
     * Create a new instance of Superslicer, which will spawn 4 regular slicer on death.
     * @param currentPathPoint integer represent the current point on the path
     * @param position         the x and y-coordinate in the map (pixel)
     * @param path             a list of point that determine the path that this instance will move
     */
    public SuperSlicer(int currentPathPoint, Point position, List<Point> path) {
        super(NAME, SPEED, HEALTH, PENALTY, REWARD, currentPathPoint, IMG, position, path);
    }

    /**
     * This event run when this enemy get killed.
     * For this type of enemy, its death will reward the player with money and spawned 2 regular slicers at its death
     * location.
     */
    @Override
    public void deathEvent() {
        super.deathEvent();
        ShadowDefend.getCurrentLevel().addSplitEnemy(new RegularSlicer(getCurrentPathPoint(), getPosition(), getPath()));
        ShadowDefend.getCurrentLevel().addSplitEnemy(new RegularSlicer(getCurrentPathPoint(), getPosition(), getPath()));
    }
}



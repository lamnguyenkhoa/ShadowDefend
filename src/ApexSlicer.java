import bagel.Image;
import bagel.util.Point;

import java.util.List;

public class ApexSlicer extends Enemy {
    private static final String NAME = "apexslicer";
    private static final double SPEED = 0.75;
    private static final int HEALTH = 25;
    private static final int PENALTY = 16;
    private static final int REWARD = 150;
    private static final Image IMG = new Image("res/images/apexslicer.png");

    /**
     * Create a new instance of Apexslicer, which will spawn 4 Megaslicer on death.
     * @param currentPathPoint integer represent the current point on the path
     * @param position         the x and y-coordinate in the map (pixel)
     * @param path             a list of point that determine the path that this instance will move
     */
    public ApexSlicer(int currentPathPoint, Point position, List<Point> path) {
        super(NAME, SPEED, HEALTH, PENALTY, REWARD, currentPathPoint, IMG, position, path);
    }

    /**
     * This event run when this enemy get killed.
     * For this type of enemy, its death will reward the player with money and spawned 4 mega slicers at its death
     * location.
     */
    @Override
    public void deathEvent() {
        super.deathEvent();
        ShadowDefend.getCurrentLevel().addSplitEnemy(new MegaSlicer(getCurrentPathPoint(), getPosition(), getPath()));
        ShadowDefend.getCurrentLevel().addSplitEnemy(new MegaSlicer(getCurrentPathPoint(), getPosition(), getPath()));
        ShadowDefend.getCurrentLevel().addSplitEnemy(new MegaSlicer(getCurrentPathPoint(), getPosition(), getPath()));
        ShadowDefend.getCurrentLevel().addSplitEnemy(new MegaSlicer(getCurrentPathPoint(), getPosition(), getPath()));
    }
}



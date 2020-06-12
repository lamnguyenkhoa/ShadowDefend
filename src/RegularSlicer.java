import bagel.Image;
import bagel.util.Point;

import java.util.List;

public class RegularSlicer extends Enemy {
    private static final String NAME = "slicer";
    private static final double SPEED = 2.0;
    private static final int HEALTH = 1;
    private static final int PENALTY = 1;
    private static final int REWARD = 2;
    private static final Image IMG = new Image("res/images/slicer.png");

    /**
     * Create a new instance of regular slicer.
     * @param currentPathPoint integer represent the current point on the path
     * @param position         the x and y-coordinate in the map (pixel)
     * @param path             a list of point that determine the path that this instance will move
     */
    public RegularSlicer(int currentPathPoint, Point position, List<Point> path) {
        super(NAME, SPEED, HEALTH, PENALTY, REWARD, currentPathPoint, IMG, position, path);
    }
}

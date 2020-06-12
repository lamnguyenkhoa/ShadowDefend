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

    public RegularSlicer(int currentPathPoint, Point position, List<Point> path) {
        super(NAME, SPEED, HEALTH, PENALTY, REWARD, currentPathPoint, IMG, position, path);
    }


}

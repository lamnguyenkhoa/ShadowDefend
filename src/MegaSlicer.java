import bagel.Image;
import bagel.util.Point;

import java.util.List;

public class MegaSlicer extends Enemy {
    private static final String NAME = "megaslicer";
    private static final double SPEED = 1.5;
    private static final int HEALTH = 2;
    private static final int PENALTY = 4;
    private static final int REWARD = 10;
    private static final Image IMG = new Image("res/images/megaslicer.png");

    public MegaSlicer(int currentPathPoint, Point position, List<Point> path) {
        super(NAME, SPEED, HEALTH, PENALTY, REWARD, currentPathPoint, IMG, position, path);
    }

    @Override
    public void deathEvent() {
        super.deathEvent();
        ShadowDefend.getCurrentLevel().addSplitEnemy(new SuperSlicer(getCurrentPathPoint(), getPosition(), getPath()));
        ShadowDefend.getCurrentLevel().addSplitEnemy(new SuperSlicer(getCurrentPathPoint(), getPosition(), getPath()));
    }
}



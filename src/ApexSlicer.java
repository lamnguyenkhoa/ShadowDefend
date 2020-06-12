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

    public ApexSlicer(int currentPathPoint, Point position, List<Point> path) {
        super(NAME, SPEED, HEALTH, PENALTY, REWARD, currentPathPoint, IMG, position, path);
    }

    @Override
    public void deathEvent() {
        super.deathEvent();
        ShadowDefend.getCurrentLevel().addSplitEnemy(new MegaSlicer(getCurrentPathPoint(), getPosition(), getPath()));
        ShadowDefend.getCurrentLevel().addSplitEnemy(new MegaSlicer(getCurrentPathPoint(), getPosition(), getPath()));
        ShadowDefend.getCurrentLevel().addSplitEnemy(new MegaSlicer(getCurrentPathPoint(), getPosition(), getPath()));
        ShadowDefend.getCurrentLevel().addSplitEnemy(new MegaSlicer(getCurrentPathPoint(), getPosition(), getPath()));
    }
}



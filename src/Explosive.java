import bagel.Image;
import bagel.util.Point;

import java.util.List;

public class Explosive extends Projectile {
    private static final int DAMAGE = 500;
    private static final int RADIUS = 200;
    private static final Image IMG = new Image("res/images/explosive.png");
    private static final int TIMER = 2;
    private int timerCounter = 0;

    /**
     * Create a new instance of Explosive
     * @param position the x and y-coordinate on the map (in pixel)
     */
    public Explosive(Point position) {
        super(IMG, position);
    }

    /**
     * After enough time passed, it will calculate this explosive's distance to other enemies. If enemies are within in
     * effect radius, they will be inflicted with damage. The explosive then removed from the game.
     */
    @Override
    public void update() {
        super.update();
        timerCounter += ShadowDefend.getTimeScale();
        if (timerCounter/ShadowDefend.getFPS() >= TIMER) {
            // BOOM!
            timerCounter = 0;
            List<Enemy> enemyList = ShadowDefend.getCurrentLevel().getEnemyList();
            for (Enemy enemy : enemyList) {
                double tmpDistance = this.getPosition().distanceTo(enemy.getPosition());
                if (tmpDistance <= RADIUS) {
                    enemy.reduceHealth(DAMAGE);
                }
            }
            setFinished(true);
        }
    }
}

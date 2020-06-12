import bagel.Image;
import bagel.Input;
import bagel.util.Point;

import java.util.List;

public class Tank extends Tower{
    private static final boolean IS_TYPE_ACTIVE = true;
    private static final int RADIUS = 100;
    private static final int DAMAGE = 1;
    private static final int COOLDOWN = 1000; //microsecond
    private static final int PRICE = 250;
    private static final Image IMG = new Image("res/images/tank.png");
    private static final Image PROJECTILE_IMG = new Image("res/images/tank_projectile.png");
    private int cooldownCounter = 0;
    private static final double FPS = ShadowDefend.getFPS();

    public Tank(Point position) {
        super(IS_TYPE_ACTIVE, PRICE, IMG, position);
    }

    @Override
    public void update() {
        super.update();
        cooldownCounter += ShadowDefend.getTimeScale();
        if ((cooldownCounter / FPS) * 1000 >= COOLDOWN) {
            cooldownCounter = 0;
            shoot();
        }
    }

    public void shoot() {
        // Shoot the nearest slicer
        List<Enemy> enemyList = ShadowDefend.getCurrentLevel().getEnemyList();
        Enemy target = null;
        double maxDistance = -1;

        // Search the closest enemy
        for (Enemy enemy : enemyList) {
            // Estimated distance from the hitbox of enemy to this tank center
            double tmpDistance = this.getPosition().distanceTo(enemy.getPosition()) - enemy.getImg().getWidth()/2;
            if (tmpDistance <= RADIUS) {
                if (tmpDistance < maxDistance || maxDistance == -1) {
                    maxDistance = tmpDistance;
                    target = enemy;
                }
            }
        }

        // Shoot
        if (target != null) {
            Point targetPoint = target.getPosition();
            setAngle(Math.atan2(targetPoint.y - this.getPosition().y, targetPoint.x - this.getPosition().x) + Math.PI/2);
            ShadowDefend.getCurrentLevel().addProjectile(new TankBullet(target, DAMAGE, PROJECTILE_IMG, getPosition()));
        }
    }


}

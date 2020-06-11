import bagel.Image;
import bagel.util.Point;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class TankBullet extends Projectile {
    private static final int SPEED = 10;
    private final Enemy TARGET;
    private final int DAMAGE;

    public TankBullet(Enemy TARGET, int DAMAGE, Image IMG, Point position) {
        super(IMG, position);
        this.TARGET = TARGET;
        this.DAMAGE = DAMAGE;
    }

    @Override
    public void update() {
        super.update();
        calculatePosition();
    }

    public void calculatePosition() {
        //TODO: Change hitbox to bounding box instead
        double deltaX;
        double deltaY;
        Point position = this.getPosition();

        Point target = TARGET.getPosition();
        double timeScale = ShadowDefend.getTimeScale();
        double totalSpeed = SPEED * timeScale;
        double distance = sqrt(pow(target.x - position.x, 2) + pow(target.y - position.y, 2));
        // If shorter than totalSpeed, teleport it directly there
        if (distance <= totalSpeed) {
            position = new Point(target.x, target.y);
            setPosition(position);
            setFinished(true);
            TARGET.reduceHealth(DAMAGE);
            return;
        }
        // Else do normal
        if (target.x == position.x) {
            deltaX = 0;
            if (target.y > position.y) {
                deltaY = totalSpeed;
            } else {
                deltaY = -totalSpeed;
            }
        } else {
            double slope = (target.y - position.y) / (target.x - position.x);
            deltaX = sqrt(pow(totalSpeed, 2) / (pow(slope, 2) + 1));
            if (target.x < position.x)
                deltaX = -deltaX;
            deltaY = deltaX * slope;
        }

        // Update bullet position
        position = new Point(position.x + deltaX, position.y + deltaY);
        setPosition(position);
    }
}

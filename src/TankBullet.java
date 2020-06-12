import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class TankBullet extends Projectile {
    private static final int SPEED = 10;
    private final Enemy TARGET;
    private final int DAMAGE;

    /**
     * Create a new instance of TankBullet
     * @param TARGET   Enemy class. Represent the enemy that this bullet is moving toward
     * @param DAMAGE   how much damage the bullet will inflict on the enemy when hit
     * @param IMG      the image used to draw the bullet on map
     * @param position the x and y-coordinate on the map (in pixel)
     */
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

    /**
     * If in contact with the enemy hitbox, reduce the enemy's health and remove the bullet from the game.
     */
    public void hitEnemy() {
        TARGET.reduceHealth(DAMAGE);
        setFinished(true);
    }

    /**
     * Determine the bullet position in the enxt frame / update() call. If it within the enemy hitbox, run the
     * hitEnemy() function.
     */
    public void calculatePosition() {
        double deltaX;
        double deltaY;
        Point position = this.getPosition();

        Point target = TARGET.getPosition();
        double timeScale = ShadowDefend.getTimeScale();
        double totalSpeed = SPEED * timeScale;
        double distance = sqrt(pow(target.x - position.x, 2) + pow(target.y - position.y, 2));
        // If shorter than totalSpeed, teleport it directly there
        // This is direct hit into the center of enemy hitbox
        if (distance <= totalSpeed) {
            position = new Point(target.x, target.y);
            setPosition(position);
            hitEnemy();
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
        // Check if it hit the enemy hitbox
        position = new Point(position.x + deltaX, position.y + deltaY);
        setPosition(position);
        Rectangle hitbox = TARGET.getHitbox();
        if (hitbox.intersects(position)) {
            hitEnemy();
        }
    }
}

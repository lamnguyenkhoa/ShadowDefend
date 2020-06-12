import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;


import java.util.List;

import static java.lang.Math.*;
import static java.lang.Math.PI;

public abstract class Enemy {
    private double speed;
    private int health;
    private int penalty;
    private int reward;
    private Image img;
    private boolean finished = false;
    private int currentPathPoint;
    private Point position;
    private final List<Point> path;
    private DrawOptions drawOptions = new DrawOptions();
    private final String name;

    //NOTE: Always calculateRotation before calculatePosition

    /**
     * Create a new instance of destructible enemy, which will grant reward if killed and will reduce lives if managed
     * to reach the finish.
     * name, speed, health, penalty and reward are constant that it will get from its children class.
     * @param name             type of enemy
     * @param speed            how many pixel it move each frame / update() call
     * @param health           its survivability
     * @param penalty          how much it will damage your lives if it managed to the finish line
     * @param reward           how much money you will get if you killed it in time
     * @param currentPathPoint integer represent the current point on the path
     * @param img              the image that will graphically represent this enemy
     * @param position         the x and y-coordinate on the map (in pixel)
     * @param path             a list of point that determine the path that this instance will move
     */
    public Enemy(String name, double speed, int health, int penalty, int reward, int currentPathPoint, Image img, Point position, List<Point> path) {
        this.name = name;
        this.speed = speed;
        this.health = health;
        this.penalty = penalty;
        this.reward = reward;
        this.currentPathPoint = currentPathPoint;
        this.img = img;
        this.position = position;
        this.path = path;
    }

    public void update() {
        calculateRotation();
        calculatePosition();
        draw();
    }

    public void draw() {
        img.draw(position.x, position.y, drawOptions);
    }

    /**
     * Take into account the timescale and calculate the position of this instance in the next frame and update it.
     */
    public void calculatePosition() {
        double deltaX;
        double deltaY;

        Point nextPoint = path.get(currentPathPoint+1);
        double timeScale = ShadowDefend.getTimeScale();
        double totalSpeed = speed * timeScale;
        double distance = sqrt(pow(nextPoint.x - position.x, 2) + pow(nextPoint.y - position.y, 2));
        // If shorter than totalSpeed, teleport it directly there
        if (distance <= totalSpeed) {
            position = new Point(nextPoint.x, nextPoint.y);
            currentPathPoint++;
            // If finished the path
            if (currentPathPoint+1 >= path.size()) {
                finished = true;
                ShadowDefend.changeLives(-penalty);
            }
            return;
        }
        // Else do normal
        if (nextPoint.x == position.x) {
            deltaX = 0;
            if (nextPoint.y > position.y) {
                deltaY = totalSpeed;
            } else {
                deltaY = -totalSpeed;
            }
        } else {
            double slope = (nextPoint.y - position.y) / (nextPoint.x - position.x);
            deltaX = sqrt(pow(totalSpeed, 2) / (pow(slope, 2) + 1));
            if (nextPoint.x < position.x)
                deltaX = -deltaX;
            deltaY = deltaX * slope;
        }
        // Update slicer pos
        position = new Point(position.x + deltaX, position.y + deltaY);
    }

    /**
     * Calculate the rotation angle of this instance in the next frame and update it. Use radian.
     */
    public void calculateRotation() {
        Point nextPoint = path.get(currentPathPoint+1);
        double distance = sqrt(pow(nextPoint.x - position.x, 2) + pow(nextPoint.y - position.y, 2));
        double radian = asin(abs(nextPoint.y - position.y)/distance);
        if ((nextPoint.x < position.x) && (nextPoint.y < position.y)) // upper left quadrant
            radian = PI - radian;
        else if ((nextPoint.x <= position.x) && (nextPoint.y >= position.y)) // lower left quadrant
            radian = radian + PI;
        else if ((nextPoint.x > position.x) && (nextPoint.y > position.y) ) // lower right quadrant
            radian = 2*PI - radian;

        // because bagel rotation is clockwise and we calculated counterclockwise above
        drawOptions.setRotation(-radian);
    }

    /**
     * This event run when this enemy get killed.
     * By default, a enemy's death will reward the player with some money.
     *
     */
    public void deathEvent() {
        ShadowDefend.changeMoney(reward);
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    /**
     * Reduce the health of this enemy. If equal or lower than zero, the enemy will be considered dead and will be
     * removed from the game.
     * @param amount integer determine how much health reduced
     */
    public void reduceHealth(int amount) {
        // Already dead :(
        if (finished) {
            return;
        }
        // Still alive~
        health -= amount;
        if (health <= 0) {
            finished = true;
            deathEvent();
        }
    }

    public boolean isFinished() {
        return finished;
    }

    public int getCurrentPathPoint() {
        return currentPathPoint;
    }

    public Point getPosition() {
        return position;
    }

    public List<Point> getPath() {
        return path;
    }

    public Rectangle getHitbox() {
        return img.getBoundingBoxAt(position);
    }

    public Image getImg() {
        return img;
    }

    public String getName() {
        return name;
    }
}

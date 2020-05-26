import bagel.DrawOptions;
import bagel.util.Point;

import static java.lang.Math.*;

public class Slicer {
    private final int BASE_SPEED = 1;
    private boolean finished = false;
    private int currentPathPoint;
    private Point position;
    private DrawOptions drawOptions;

    /* Constructor */
    public Slicer(int currentPathPoint, Point position, DrawOptions drawOptions) {
        this.currentPathPoint = currentPathPoint;
        this.position = position;
        this.drawOptions = drawOptions;
    }

    /* Methods */
    public void calculateRotation() {
        Point nextPoint = ShadowDefend.getPath().get(currentPathPoint+1);
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

    public void calculatePosition() {
        double deltaX;
        double deltaY;

        Point nextPoint = ShadowDefend.getPath().get(currentPathPoint+1);
        int timeScale = ShadowDefend.getTimeScale();
        int totalSpeed = BASE_SPEED * timeScale;
        double distance = sqrt(pow(nextPoint.x - position.x, 2) + pow(nextPoint.y - position.y, 2));
        // If shorter than totalSpeed, teleport it directly there
        if (distance <= totalSpeed) {
            position = new Point(nextPoint.x, nextPoint.y);
            currentPathPoint++;
            // If finished the path
            if (currentPathPoint+1 >= ShadowDefend.getPath().size()) {
                finished = true;
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

    /* Getters and Setters */
    public int getSpeed() {
        return BASE_SPEED;
    }

    public int getCurrentPathPoint() {
        return currentPathPoint;
    }

    public void setCurrentPathPoint(int currentPathPoint) {
        this.currentPathPoint = currentPathPoint;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public DrawOptions getDrawOptions() {
        return drawOptions;
    }

    public void setDrawOptions(DrawOptions drawOptions) {
        this.drawOptions = drawOptions;
    }

    public boolean isFinished() {
        return finished;
    }
}



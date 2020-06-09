import bagel.Image;
import bagel.Input;
import bagel.util.Point;

public abstract class Projectile {
    private Image img;
    private Point position;
    private boolean finished = false;

    public Projectile(Image img, Point position) {
        this.img = img;
        this.position = position;
    }

    public void update() {
        draw();
    }

    public void draw() {
        img.draw(position.x, position.y);
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public boolean isFinished() {
        return finished;
    }
}

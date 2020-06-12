import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Point;

public abstract class Tower {
    private boolean isActiveType;
    private int price;
    private Image img;
    private Point position;
    private DrawOptions drawOptions = new DrawOptions();
    private boolean finished = false;

    /**
     * Create a new instance of Tower.
     * isActiveType, price and img are constant that it will get from its children class
     * @param isActiveType boolean that determine if the tower is an active type or passive type
     * @param price        how much money required to purchase the tower
     * @param img          the image that represent the tower on the map
     * @param position     the x and y-coordinate where to draw the tower on the map (in pixel)
     */
    public Tower(boolean isActiveType, int price, Image img, Point position) {
        this.isActiveType = isActiveType;
        this.price = price;
        this.img = img;
        this.position = position;
    }

    public void update() {
        draw();
    }

    public void draw() {
        img.draw(position.x, position.y, drawOptions);
    }

    public void setAngle(double angle) {
        drawOptions.setRotation(angle);
    }

    public Point getPosition() {
        return position;
    }

    public Image getImg() {
        return img;
    }

    public void move(double deltaX, double deltaY) {
        position = new Point(position.x + deltaX, position.y + deltaY);
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public boolean isFinished() {
        return finished;
    }
}

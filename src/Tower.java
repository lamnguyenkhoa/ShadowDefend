import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Point;

public abstract class Tower {
    private boolean isActiveType;
    private int price;
    private Image img;
    private Point position;
    private DrawOptions drawOptions = new DrawOptions();

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


}

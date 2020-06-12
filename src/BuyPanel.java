import bagel.DrawOptions;
import bagel.Font;
import bagel.Image;
import bagel.map.TiledMap;
import bagel.util.Colour;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.List;

public class BuyPanel {
    private static final Image BACKGROUND = new Image("res/images/buypanel.png");
    private static final Image TANK_IMG = new Image("res/images/tank.png");
    private static final Image SUPERTANK_IMG = new Image("res/images/supertank.png");
    private static final Image AIRPLANE_IMG = new Image("res/images/airsupport.png");
    private static String towerType;
    private static final Font FONT = new Font("res/fonts//DejaVuSans-Bold.ttf", 16);
    private static final Font FONT_PRICE = new Font("res/fonts/DejaVuSans-Bold.ttf", 22);
    private static final Font FONT_MONEY = new Font("res/fonts/DejaVuSans-Bold.ttf", 50);
    private static final int LEFT_ITEM_OFFSET_H = 64;
    private static final int ITEM_GAP = 120;
    private static final int CENTER_ITEM_OFFSET_V = 10;
    private static final int MONEY_OFFSET_H = 200;
    private static final int MONEY_OFFSET_V = 65;
    private static final int TANK_PRICE = 250;
    private static final int SUPERTANK_PRICE = 600;
    private static final int AIRPLANE_PRICE = 500;
    private static boolean mouseRenderOn = false;
    private static boolean airplaneHorizontal = true;
    private static final DrawOptions greenText = new DrawOptions().setBlendColour(Colour.GREEN);
    private static final DrawOptions redText = new DrawOptions().setBlendColour(Colour.RED);
    private static final double buyPanelBound = BACKGROUND.getHeight();
    private static final double statusPanelBound = ShadowDefend.getHEIGHT() - new Image("res/images/statuspanel.png").getHeight();

    public static void draw(int money, Point mousePosition) {
        // Assume Tank, SuperTank and Airplane have same size sprite
        double tmpTowerY = BACKGROUND.getHeight()/2 - CENTER_ITEM_OFFSET_V;
        double tmpPriceX = LEFT_ITEM_OFFSET_H - TANK_IMG.getWidth()/2;
        double tmpPriceY = BACKGROUND.getHeight()/2 + TANK_IMG.getHeight()/2 + 5; // +5 to make it look nicer

        // Draw buy tower menu
        BACKGROUND.drawFromTopLeft(0, 0);
        TANK_IMG.draw(LEFT_ITEM_OFFSET_H, tmpTowerY);
        SUPERTANK_IMG.draw(LEFT_ITEM_OFFSET_H + ITEM_GAP, tmpTowerY);
        AIRPLANE_IMG.draw(LEFT_ITEM_OFFSET_H + ITEM_GAP*2, tmpTowerY);
        if (money >= TANK_PRICE) {
            FONT_PRICE.drawString("$" + TANK_PRICE, tmpPriceX, tmpPriceY, greenText);
        } else {
            FONT_PRICE.drawString("$" + TANK_PRICE, tmpPriceX, tmpPriceY, redText);
        }
        if (money >= SUPERTANK_PRICE) {
            FONT_PRICE.drawString("$" + SUPERTANK_PRICE, tmpPriceX + ITEM_GAP, tmpPriceY, greenText);
        } else {
            FONT_PRICE.drawString("$" + SUPERTANK_PRICE, tmpPriceX + ITEM_GAP, tmpPriceY, redText);
        }
        if (money >= AIRPLANE_PRICE) {
            FONT_PRICE.drawString("$" + AIRPLANE_PRICE, tmpPriceX + ITEM_GAP*2, tmpPriceY, greenText);
        } else {
            FONT_PRICE.drawString("$" + AIRPLANE_PRICE, tmpPriceX + ITEM_GAP*2, tmpPriceY, redText);
        }

        // Draw key binds:
        // All number is chosen to make it look good, no special meaning
        FONT.drawString("Key binds:", BACKGROUND.getWidth()/2 - 20, 20);
        FONT.drawString("S - Start Wave", BACKGROUND.getWidth()/2 - 20, 50);
        FONT.drawString("L - Increase Timescale", BACKGROUND.getWidth()/2 - 20, 70);
        FONT.drawString("K - Decrease Timescale", BACKGROUND.getWidth()/2 - 20, 90);

        // Draw money
        FONT_MONEY.drawString("$" + money, ShadowDefend.getWIDTH() - MONEY_OFFSET_H, MONEY_OFFSET_V);

        // Draw tower at mouse location
        if (mouseRenderOn) {
            drawAtMouse(towerType, mousePosition);
        }
    }

    /**
     * If haven't clicked on any tower image in the buy panel, it will check if this left click is valid or not. If this
     * left click valid (clicked on images that represent different types of towers), it will select that tower type.
     * If already selected a tower type, it will check whether you have enough money to buy it. If you have enough, then
     * it will place the tower at the current mouse location (if this position is valid).
     * @param money         how much money you currently have
     * @param mousePosition an x and y-coordinate of current mouse position (in pixel)
     */
    public static void checkClick(int money, Point mousePosition) {
        if (!mouseRenderOn) {
            // Choose tower
            double upperBound = BACKGROUND.getHeight() / 2 - CENTER_ITEM_OFFSET_V - TANK_IMG.getHeight() / 2;
            double lowerBound = BACKGROUND.getHeight() / 2 - CENTER_ITEM_OFFSET_V + TANK_IMG.getHeight() / 2;
            if (mousePosition.y >= upperBound && mousePosition.y <= lowerBound) {
                double leftBound = LEFT_ITEM_OFFSET_H - TANK_IMG.getWidth() / 2;
                double rightBound = LEFT_ITEM_OFFSET_H + TANK_IMG.getWidth() / 2;
                if (mousePosition.x >= leftBound && mousePosition.x <= rightBound) {
                    if (money < TANK_PRICE) {
                        return;
                    }
                    mouseRenderOn = true;
                    towerType = "tank";
                    ShadowDefend.setPlacingTower(true);
                } else if (mousePosition.x >= leftBound + ITEM_GAP && mousePosition.x <= rightBound + ITEM_GAP) {
                    if (money < SUPERTANK_PRICE) {
                        return;
                    }
                    mouseRenderOn = true;
                    towerType = "supertank";
                    ShadowDefend.setPlacingTower(true);
                } else if (mousePosition.x >= leftBound + ITEM_GAP*2 && mousePosition.x <= rightBound + ITEM_GAP*2) {
                    if (money < AIRPLANE_PRICE) {
                        return;
                    }
                    mouseRenderOn = true;
                    towerType = "airplane";
                    ShadowDefend.setPlacingTower(true);
                }
            }
        } else {
            // Buy/Place tower
            if (notValidLocation(mousePosition)) {
                return;
            }
            switch (towerType) {
                case "tank":
                    ShadowDefend.changeMoney(-TANK_PRICE);
                    ShadowDefend.getCurrentLevel().addTower(new Tank(mousePosition));
                    deSelect();
                    break;
                case "supertank":
                    ShadowDefend.changeMoney(-SUPERTANK_PRICE);
                    ShadowDefend.getCurrentLevel().addTower(new SuperTank(mousePosition));
                    deSelect();
                    break;
                case "airplane":
                    ShadowDefend.changeMoney(-AIRPLANE_PRICE);
                    // Spawn outside map
                    Point tmpPoint;
                    if (airplaneHorizontal) {
                        tmpPoint = new Point(-AIRPLANE_IMG.getHeight(), mousePosition.y);
                    } else {
                        tmpPoint = new Point(mousePosition.x, -AIRPLANE_IMG.getHeight());
                    }
                    ShadowDefend.getCurrentLevel().addTower(new Airplane(tmpPoint, airplaneHorizontal));
                    deSelect();
                    // Next airplane direction is switched
                    airplaneHorizontal = !airplaneHorizontal;
                    break;
            }
        }
    }

    /**
     * Render an image that represent the tower that you are going to buy at the mouse location. It will not draw
     * if the location is not valid.
     * @param towerType     string name of the tower's type
     * @param mousePosition an x and y-coordinate of current mouse position (in pixel)
     */
    public static void drawAtMouse(String towerType, Point mousePosition) {
        // Check validity
        if (notValidLocation(mousePosition)) {
            return;
        }

        // Check type tower
        switch (towerType) {
            case "tank":
                TANK_IMG.draw(mousePosition.x, mousePosition.y);
                break;
            case "supertank":
                SUPERTANK_IMG.draw(mousePosition.x, mousePosition.y);
                break;
            case "airplane":
                if (airplaneHorizontal) {
                    AIRPLANE_IMG.draw(mousePosition.x, mousePosition.y, new DrawOptions().setRotation(Math.PI/2));
                } else {
                    AIRPLANE_IMG.draw(mousePosition.x, mousePosition.y, new DrawOptions().setRotation(Math.PI));
                }
                break;
        }
    }

    public static boolean notValidLocation(Point mousePosition) {
        TiledMap map = ShadowDefend.getCurrentLevel().getTiledMap();

        // If mouse outside game window
        if (mousePosition.x > ShadowDefend.getWIDTH() || mousePosition.y > ShadowDefend.getHEIGHT() ||
        mousePosition.x < 0 || mousePosition.y < 0) {
            return  true;
        }
        // If the current map tile has blocked property
        if (map.hasProperty((int) mousePosition.x, (int) mousePosition.y, "blocked")) {
            return true;
        }
        // If current mouse position is in status panel or buy panel
        if (mousePosition.y <= buyPanelBound || mousePosition.y >= statusPanelBound) {
            return true;
        }
        // Default case
        List<Tower> towerList = ShadowDefend.getCurrentLevel().getTowerList();
        for (Tower tower: towerList) {
            Rectangle tmpRect = tower.getImg().getBoundingBoxAt(tower.getPosition());
            if (tmpRect.intersects(mousePosition)) {
                return true;
            }
        }
        return false;
    }

    public static void deSelect() {
        mouseRenderOn = false;
        ShadowDefend.setPlacingTower(false);
    }
}

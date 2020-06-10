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
            FONT_PRICE.drawString("$" + Integer.toString(TANK_PRICE), tmpPriceX, tmpPriceY, greenText);
        } else {
            FONT_PRICE.drawString("$" + Integer.toString(TANK_PRICE), tmpPriceX, tmpPriceY, redText);
        }
        if (money >= SUPERTANK_PRICE) {
            FONT_PRICE.drawString("$" + Integer.toString(SUPERTANK_PRICE), tmpPriceX + ITEM_GAP, tmpPriceY, greenText);
        } else {
            FONT_PRICE.drawString("$" + Integer.toString(SUPERTANK_PRICE), tmpPriceX + ITEM_GAP, tmpPriceY, redText);
        }
        if (money >= AIRPLANE_PRICE) {
            FONT_PRICE.drawString("$" + Integer.toString(AIRPLANE_PRICE), tmpPriceX + ITEM_GAP*2, tmpPriceY, greenText);
        } else {
            FONT_PRICE.drawString("$" + Integer.toString(AIRPLANE_PRICE), tmpPriceX + ITEM_GAP*2, tmpPriceY, redText);
        }

        // Draw key binds:
        // All number is chosen to make it look good, no special meaning
        FONT.drawString("Key binds:", BACKGROUND.getWidth()/2 - 20, 20);
        FONT.drawString("S - Start Wave", BACKGROUND.getWidth()/2 - 20, 50);
        FONT.drawString("L - Increase Timescale", BACKGROUND.getWidth()/2 - 20, 70);
        FONT.drawString("K - Decrease Timescale", BACKGROUND.getWidth()/2 - 20, 90);

        // Draw money
        FONT_MONEY.drawString("$" + Integer.toString(money), ShadowDefend.getWIDTH() - MONEY_OFFSET_H, MONEY_OFFSET_V);

        // Draw tower at mouse location
        if (mouseRenderOn) {
            drawAtMouse(towerType, mousePosition);
        }
    }


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
            // TODO: Add Airplane
            if (!checkLocationValidity(mousePosition)) {
                return;
            }
            if (towerType.equals("tank")) {
                ShadowDefend.changeMoney(-TANK_PRICE);
                ShadowDefend.getCurrentLevel().addTower(new Tank(mousePosition));
                deSelect();
            } else if (towerType.equals("supertank")) {
                ShadowDefend.changeMoney(-TANK_PRICE);
                ShadowDefend.getCurrentLevel().addTower(new SuperTank(mousePosition));
                deSelect();
            }
        }
    }

    public static void drawAtMouse(String towerType, Point mousePosition) {
        // Check validity
        if (!checkLocationValidity(mousePosition)) {
            return;
        }

        // Check type tower
        if (towerType.equals("tank")) {
            TANK_IMG.draw(mousePosition.x, mousePosition.y);
        } else if (towerType.equals("supertank")) {
            SUPERTANK_IMG.draw(mousePosition.x, mousePosition.y);
        } else if (towerType.equals("airplane")) {
            AIRPLANE_IMG.draw(mousePosition.x, mousePosition.y);
        }
    }

    public static boolean checkLocationValidity(Point mousePosition) {
        TiledMap map = ShadowDefend.getCurrentLevel().getTiledMap();
        if (map.hasProperty((int) mousePosition.x, (int) mousePosition.y, "blocked")) {
            return false;
        }
        if (mousePosition.y <= buyPanelBound || mousePosition.y >= statusPanelBound) {
            return false;
        }
        List<Tower> towerList = ShadowDefend.getCurrentLevel().getTowerList();
        for (Tower tower: towerList) {
            Rectangle tmpRect = tower.getImg().getBoundingBoxAt(tower.getPosition());
            if (tmpRect.intersects(mousePosition)) {
                return false;
            }
        }
        return true;
    }

    public static void deSelect() {
        mouseRenderOn = false;
        ShadowDefend.setPlacingTower(false);
    }
}

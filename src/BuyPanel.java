import bagel.DrawOptions;
import bagel.Font;
import bagel.Image;
import bagel.util.Colour;

public class BuyPanel {
    private static final Image BACKGROUND = new Image("res/images/buypanel.png");
    private static final Image TANK_IMG = new Image("res/images/tank.png");
    private static final Image SUPERTANK_IMG = new Image("res/images/supertank.png");
    private static final Image AIRPLANE_IMG = new Image("res/images/airsupport.png");
    private static final Font FONT = new Font("res/fonts//DejaVuSans-Bold.ttf", 16);
    private static final Font FONT_PRICE = new Font("res/fonts//DejaVuSans-Bold.ttf", 22);
    private static final Font FONT_MONEY = new Font("res/fonts//DejaVuSans-Bold.ttf", 50);
    private static final int LEFT_ITEM_OFFSET_H = 64;
    private static final int ITEM_GAP = 120;
    private static final int CENTER_ITEM_OFFSET_V = 10;
    private static final int MONEY_OFFSET_H = 200;
    private static final int MONEY_OFFSET_V = 65;
    private static final int TANK_PRICE = 250;
    private static final int SUPERTANK_PRICE = 600;
    private static final int AIRPLANE_PRICE = 500;

    public static void draw(int money) {
        double tmpTowerY = BACKGROUND.getHeight()/2 - CENTER_ITEM_OFFSET_V;
        double tmpPriceX = LEFT_ITEM_OFFSET_H - TANK_IMG.getWidth()/2;
        double tmpPriceY = BACKGROUND.getHeight()/2 + TANK_IMG.getHeight()/2 + 5; // +5 to make it look nicer

        // Draw buy tower menu
        BACKGROUND.drawFromTopLeft(0, 0);
        TANK_IMG.draw(LEFT_ITEM_OFFSET_H, tmpTowerY);
        SUPERTANK_IMG.draw(LEFT_ITEM_OFFSET_H + ITEM_GAP, tmpTowerY);
        AIRPLANE_IMG.draw(LEFT_ITEM_OFFSET_H + ITEM_GAP*2, tmpTowerY);
        if (money >= TANK_PRICE) {
            FONT_PRICE.drawString("$" + Integer.toString(TANK_PRICE), tmpPriceX, tmpPriceY, new DrawOptions().setBlendColour(Colour.GREEN));
        } else {
            FONT_PRICE.drawString("$" + Integer.toString(TANK_PRICE), tmpPriceX, tmpPriceY, new DrawOptions().setBlendColour(Colour.RED));
        }
        if (money >= SUPERTANK_PRICE) {
            FONT_PRICE.drawString("$" + Integer.toString(SUPERTANK_PRICE), tmpPriceX + ITEM_GAP, tmpPriceY, new DrawOptions().setBlendColour(Colour.GREEN));
        } else {
            FONT_PRICE.drawString("$" + Integer.toString(SUPERTANK_PRICE), tmpPriceX + ITEM_GAP, tmpPriceY, new DrawOptions().setBlendColour(Colour.RED));
        }
        if (money >= AIRPLANE_PRICE) {
            FONT_PRICE.drawString("$" + Integer.toString(AIRPLANE_PRICE), tmpPriceX + ITEM_GAP*2, tmpPriceY, new DrawOptions().setBlendColour(Colour.GREEN));
        } else {
            FONT_PRICE.drawString("$" + Integer.toString(AIRPLANE_PRICE), tmpPriceX + ITEM_GAP*2, tmpPriceY, new DrawOptions().setBlendColour(Colour.RED));
        }

        // Draw key binds:
        // All number is chosen to make it look good, no special meaning
        FONT.drawString("Key binds:", BACKGROUND.getWidth()/2 - 20, 20);
        FONT.drawString("S - Start Wave", BACKGROUND.getWidth()/2 - 20, 50);
        FONT.drawString("L - Increase Timescale", BACKGROUND.getWidth()/2 - 20, 70);
        FONT.drawString("K - Decrease Timescale", BACKGROUND.getWidth()/2 - 20, 90);

        // Draw money
        FONT_MONEY.drawString("$" + Integer.toString(money), ShadowDefend.getWIDTH() - MONEY_OFFSET_H, MONEY_OFFSET_V);
    }
}

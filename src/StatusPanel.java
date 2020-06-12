import bagel.DrawOptions;
import bagel.Font;
import bagel.Image;
import bagel.util.Colour;
import bagel.util.Point;

public class StatusPanel {
    private static final Image BACKGROUND = new Image("res/images/statuspanel.png");
    private static final Font FONT = new Font("res/fonts/DejaVuSans-Bold.ttf", 16);
    private static final double POS_X = 0;
    private static final double POS_Y = ShadowDefend.getHEIGHT() - BACKGROUND.getHeight();
    private static final DrawOptions greenText = new DrawOptions().setBlendColour(Colour.GREEN);

    public static void draw(int waveID, double timeScale, int lives, boolean gameWon, boolean placingTower, boolean waveInProgress) {
        BACKGROUND.drawFromTopLeft(POS_X, POS_Y);
        FONT.drawString("Wave: " + waveID, POS_X + 12, POS_Y + 18);
        if (timeScale > 1) {
            FONT.drawString("Time Scale: " + timeScale, POS_X + 200, POS_Y + 18, greenText);
        } else {
            FONT.drawString("Time Scale: " + timeScale, POS_X + 200, POS_Y + 18);
        }
        if (gameWon) {
            FONT.drawString("Status: Winner!", POS_X + 450, POS_Y + 18);
        } else if (placingTower) {
            FONT.drawString("Status: Placing", POS_X + 450, POS_Y + 18);
        } else if (waveInProgress) {
            FONT.drawString("Status: Wave In Progress", POS_X + 450, POS_Y + 18);
        } else {
            FONT.drawString("Status: Awaiting Start", POS_X + 450, POS_Y + 18);
        }
        FONT.drawString("Lives: " + lives, POS_X + 925, POS_Y + 18);
    }

}

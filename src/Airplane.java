import bagel.Image;
import bagel.util.Point;

import java.util.Random;


public class Airplane extends Tower {
    private static final Image IMG = new Image("res/images/airsupport.png");
    private static final int SPEED = 3;
    private static final boolean IS_ACTIVE_TYPE = false;
    private static final int PRICE = 500;
    private final boolean IS_HORIZONTAL;
    private int timer = 0;
    private int timerCounter = 0;
    private Random random = new Random();

    public Airplane(Point position, boolean isHorizontal) {
        super(IS_ACTIVE_TYPE, PRICE, IMG, position);
        this.IS_HORIZONTAL = isHorizontal;
        if (IS_HORIZONTAL) {
            setAngle(Math.PI/2);
        } else {
            setAngle(Math.PI);
        }
        randomizeTimer();
    }

    @Override
    public void update() {
        super.update();
        calculatePosition();
        timerCounter += ShadowDefend.getTimeScale();
        if (timerCounter/ShadowDefend.getFPS() >= timer) {
            timerCounter = 0;
            if(!isOutsideMap()) {
                dropExplosive();
            }
        }
    }

    public void randomizeTimer() {
        timer = random.nextInt(2); // get random int from 0-1
        timer += 1; //+1 so value from 1-2
    }

    public void dropExplosive() {
        ShadowDefend.getCurrentLevel().addProjectile(new Explosive(getPosition()));
        randomizeTimer();
    }

    public void calculatePosition() {
        if (IS_HORIZONTAL) {
            move(SPEED*ShadowDefend.getTimeScale(), 0);
        } else {
            move(0, SPEED*ShadowDefend.getTimeScale());
        }
        // If outside the map, delete it
        if (isOutsideMap()) {
            setFinished(true);
        }
    }

    public boolean isOutsideMap() {
        return getPosition().x > ShadowDefend.getWIDTH() || getPosition().y > ShadowDefend.getHEIGHT();
    }
}
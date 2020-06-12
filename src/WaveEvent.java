public class WaveEvent {
    private int id;
    private String eventType;
    private int quantity;
    private String enemyType;
    private int delay;

    /**
     * Create a new instance of WaveEvent. If only have 3 parameters, it's a delay event.
     * @param id        integer that used to identify which wave this wave event belong to
     * @param eventType string that represent the type of wave event. For this one, it's "delay"
     * @param delay     how much time (in microsecond) the game need to wait until next wave event
     */
    public WaveEvent(int id, String eventType, int delay) {
        this.id = id;
        this.eventType = eventType;
        this.delay = delay;
    }

    /**
     *  Create a new instance of WaveEvent. If have 5 parameters, it's a spawn event.
     * @param id        integer that used to identify which wave this wave event belong to
     * @param eventType string that represent the type of wave event. For this one, it's "delay"
     * @param quantity  number of enemies that this wave event will spawn (not included enemies spawned by death)
     * @param enemyType string that represent the type of enemy that this wave event spawn
     * @param delay     how much time (in microsecond) the game need to wait between spawning enemy
     */
    public WaveEvent(int id, String eventType, int quantity, String enemyType, int delay) {
        this.id = id;
        this.eventType = eventType;
        this.quantity = quantity;
        this.enemyType = enemyType;
        this.delay = delay;
    }

    public int getId() {
        return id;
    }

    public String getEventType() {
        return eventType;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getEnemyType() {
        return enemyType;
    }

    public int getDelay() {
        return delay;
    }

    public void reduceQuantity(int amount) {
        quantity -= amount;
    }
}

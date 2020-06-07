public class WaveEvent {
    private int id;
    private String eventType;
    private int quantity;
    private String enemyType;
    private int delay;

    public WaveEvent(int id, String eventType, int delay) {
        this.id = id;
        this.eventType = eventType;
        this.delay = delay;
    }

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
}

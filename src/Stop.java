
public class Stop {

    private final String stopId;
    private final String name;
    private final float posLat;
    private final float posLong;

    public Stop(String stopId, String name, float posLat, float posLong) {
        this.stopId = stopId;
        this.name = name;
        this.posLat = posLat;
        this.posLong = posLong;
    }

    public String getStopId() {
        return stopId;
    }

    public String getName() {
        return name;
    }

    public float getPosLat() {
        return posLat;
    }

    public float getPosLon() {
        return posLong;
    }

}

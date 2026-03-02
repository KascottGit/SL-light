
public class Stop {

    private String stopId;
    private String name;
    private float posLat;
    private float posLong;

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

    public float getPosLong() {
        return posLong;
    }

}

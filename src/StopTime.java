
public class StopTime implements Comparable<StopTime> {

    private final String stopId;
    private final int departureTime;

    private final int stopType;

    private final String tripId;
    private final int stopSequence;

    public StopTime(String stopId, int departureTime, int stopType, String tripId, int stopSequence) {
        this.stopId = stopId;
        this.departureTime = departureTime;
        this.stopType = stopType; //0 for only drop off, 1 both, 2 for only pick upp. 
        this.tripId = tripId;
        this.stopSequence = stopSequence;
    }

    public String getStopId() {
        return stopId;
    }

    public int getDepartureTime() {
        return departureTime;
    }

    public int getStopType() {
        return stopType;
    }

    public String getTripId() {
        return tripId;
    }

    public int getStopSequence() {
        return stopSequence;
    }

    @Override
    public String toString() {
        return "[" + stopId + "]";
    }

    @Override
    public int compareTo(StopTime other) {
        return departureTime - other.getDepartureTime();
    }
}

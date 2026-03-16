
public class StopTime implements Comparable<StopTime> {

    private String stopId;
    private int departureTime;

    private int stopType;

    private String tripId;
    private int stopSequence;

    public StopTime(String stopId, int departureTime, int stopType, String tripId, int stopSequence) {
        this.stopId = stopId; //Refers to the stop
        this.departureTime = departureTime; //When it arrives at the stop
        this.stopType = stopType; //0 for only drop off, 1 for drop off and pick up, 2 for only pick upp. 
        this.tripId = tripId; //Refers to the trip, to connect to next stop
        this.stopSequence = stopSequence; //To find next stop in the trip
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
    public int compareTo(StopTime o) {
        return departureTime - o.getDepartureTime();
    }
}

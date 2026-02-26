
public class StopTime {

    private long stopId;
    private int departureTime;

    private int stopType;

    private long tripId;
    private int stopSequence;

    public StopTime(long stopId, int departureTime, int stopType, long tripId, int stopSequence) {
        this.stopId = stopId; //Refers to the stop
        this.departureTime = departureTime; //When it arrives at the stop
        this.stopType = stopType; //0 for drop off and pick up, 1 for only drop off, 2 for only pick upp. 
        this.tripId = tripId; //Refers to the trip, to connect to next stop
        this.stopSequence = stopSequence; //To find next stop in the trip
    }

    public long getStopId() {
        return stopId;
    }

    public int getDepartureTime() {
        return departureTime;
    }

    public int getStopType() {
        return stopType;
    }

    public long getTripId() {
        return tripId;
    }

    public int getStopSequence() {
        return stopSequence;
    }

    @Override
    public String toString() {
        return "[" + stopId + "]";
    }
}

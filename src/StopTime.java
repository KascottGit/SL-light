
public class StopTime {

    private int stopId;
    private int departureTime;

    private int stopType;

    private int tripId;
    private int stopSequence;

    public StopTime(int stopId, int departureTime, int stopType, int tripId, int stopSequence) {
        this.stopId = stopId; //Refers to the stop
        this.departureTime = departureTime; //When it arrives at the stop
        this.stopType = stopType; //0 for drop off and pick up, 1 for only drop off, 2 for only pick upp. 
        this.tripId = tripId; //Refers to the trip, to connect to next stop
        this.stopSequence = stopSequence; //To find next stop in the trip
    }

    public int getStopId() {
        return stopId;
    }

    public int getDepartureTime() {
        return departureTime;
    }

    public int getStopType() {
        return stopType;
    }

    public int getTripId() {
        return tripId;
    }

    public int getStopSequence() {
        return stopSequence;
    }
}

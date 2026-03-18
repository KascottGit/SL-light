
public class Trip {

    private final String tripId;
    private final String routeId;
    private final String headSign;

    public Trip(String tripId, String routeId, String headSign) {
        this.tripId = tripId;
        this.routeId = routeId;
        this.headSign = headSign;
    }

    public String getTripId() {
        return tripId;
    }

    public String getRouteId() {
        return routeId;
    }

    public String getHeadSign() {
        return headSign;
    }
}

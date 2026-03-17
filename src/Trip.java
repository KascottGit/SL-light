
public class Trip {

    private String tripId;
    private String routeId;
    private String headSign;

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

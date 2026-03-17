
public class Route {

    private String routeId;
    private String shortName;
    private String longName;

    public Route(String routeId, String shortName, String longName) {
        this.routeId = routeId;
        this.shortName = shortName;
        this.longName = longName;
    }

    public String getRouteId() {
        return routeId;
    }

    public String getShortName() {
        return shortName;
    }

    public String getLongName() {
        return longName;
    }

}

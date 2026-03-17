
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransitNetwork {

    private TransitGraph graph;
    private AStarRouteFinder routeFinder;

    private HashMap<String, List<StopTime>> tripMap;
    private HashMap<String, List<StopTime>> stopMap;

    private HashMap<String, Stop> stopIdMap;
    private HashMap<String, Stop> stopNameMap;

    private HashMap<String, Trip> tripIdMap;
    private HashMap<String, Route> routeIdMap;

    public TransitNetwork() {
        graph = new TransitGraph();
        routeFinder = new AStarRouteFinder(this);
        tripMap = new HashMap<>();
        stopMap = new HashMap<>();
        stopIdMap = new HashMap<>();
        stopNameMap = new HashMap<>();
        tripIdMap = new HashMap<>();
        routeIdMap = new HashMap<>();
    }

    public void buildGraph(List<StopTime> stopTimes, List<Stop> stops, List<Trip> trips, List<Route> routes) {
        //Populate stopDirectory
        for (Stop stop : stops) {
            stopIdMap.put(stop.getStopId(), stop);
            stopNameMap.put(stop.getName().toLowerCase(), stop);
        }

        //Populate Trip Directoty
        for (Trip trip : trips) {
            tripIdMap.put(trip.getTripId(), trip);
        }

        //Populate Route Directoty
        for (Route route : routes) {
            routeIdMap.put(route.getRouteId(), route);
        }

        //Populate graph and maps
        for (StopTime stopTime : stopTimes) {
            //Add to graph
            graph.add(stopTime);
            //Map to tripId
            tripMap.computeIfAbsent(stopTime.getTripId(), k -> new ArrayList<>()).add(stopTime);
            //Map to stopId
            stopMap.computeIfAbsent(stopTime.getStopId(), k -> new ArrayList<>()).add(stopTime);
        }

        //Make connections to same trip forward
        for (Map.Entry<String, List<StopTime>> entry : tripMap.entrySet()) {
            List<StopTime> list = entry.getValue();
            list.sort(Comparator.comparingInt(StopTime::getStopSequence));

            for (int i = 0; i < list.size() - 1; i++) {
                graph.connect(list.get(i), list.get(i + 1));
            }
        }

        //Make connections to same stop forward in time
        for (Map.Entry<String, List<StopTime>> entry : stopMap.entrySet()) {
            List<StopTime> list = entry.getValue();
            list.sort(Comparator.comparingInt(StopTime::getDepartureTime));

            for (int i = 0; i < list.size() - 1; i++) {
                graph.connect(list.get(i), list.get(i + 1));
            }
        }
    }

    public List<StopTime> findRoute(Stop fromStop, Stop toStop, int departureTime) {
        if (fromStop == null || toStop == null) {
            return null;
        }

        StopTime fromStopTime = findStopTime(fromStop, departureTime);
        if (fromStopTime == null) {
            return null;
        }

        return routeFinder.findRoute(fromStopTime, toStop, graph.getGraph());
    }

    public StopTime findStopTime(Stop stop, int time) {
        if (stop == null) {
            return null;
        }

        String stopId = stop.getStopId();

        List<StopTime> stopTimes = stopMap.get(stopId);

        StopTime stopTime = null;

        for (StopTime s : stopTimes) {
            if (s.getDepartureTime() > time) {
                stopTime = s;
                break;
            }
        }

        return stopTime;
    }

    public Stop getStopByName(String name) {
        if (name == null) {
            return null;
        }
        if (!stopNameMap.containsKey(name)) {
            return null;
        }

        return stopNameMap.get(name);
    }

    public Stop getStopById(String id) {
        if (id == null) {
            return null;
        }
        if (!stopIdMap.containsKey(id)) {
            return null;
        }

        return stopIdMap.get(id);
    }

    public String getTripInformation(String tripId) {
        if (tripId == null) {
            return null;
        }
        if (!tripIdMap.containsKey(tripId)) {
            return null;
        }

        Trip trip = tripIdMap.get(tripId);
        String routeId = trip.getRouteId();

        if (!routeIdMap.containsKey(routeId)) {
            return null;
        }
        Route route = routeIdMap.get(routeId);

        String routeName = route.getLongName().isBlank() ? route.getShortName() : route.getLongName();

        String headSign = trip.getHeadSign();

        return "route " + routeName + " towards " + headSign;
    }

    public double distanceBetweenStops(String stopIdFrom, String stopIdTo) {
        if (!stopIdMap.containsKey(stopIdFrom) || !stopIdMap.containsKey(stopIdTo)) {
            return -1;
        }

        double R = 6371.0; //Earth radius in km

        Stop from = stopIdMap.get(stopIdFrom);
        double lat1 = from.getPosLat();
        double lon1 = from.getPosLon();

        Stop to = stopIdMap.get(stopIdTo);
        double lat2 = to.getPosLat();
        double lon2 = to.getPosLon();

        double phi1 = Math.toRadians(lat1);
        double phi2 = Math.toRadians(lat2);
        double dphi = Math.toRadians(lat2 - lat1);
        double dlambda = Math.toRadians(lon2 - lon1);

        //Haversine formula
        double a = Math.pow(Math.sin(dphi / 2), 2) + Math.cos(phi1) * Math.cos(phi2) * Math.pow(Math.sin(dlambda / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

}

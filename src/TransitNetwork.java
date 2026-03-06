
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

    public TransitNetwork() {
        graph = new TransitGraph();
        routeFinder = new AStarRouteFinder();
        tripMap = new HashMap<>();
        stopMap = new HashMap<>();
        stopIdMap = new HashMap<>();
        stopNameMap = new HashMap<>();
    }

    public void buildGraph(List<StopTime> stopTimes, List<Stop> stops) {
        //Populate stopDirectory
        for (Stop stop : stops) {
            stopIdMap.put(stop.getStopId(), stop);
            stopNameMap.put(stop.getName(), stop);
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

    public List<StopTime> findRoute(String startStop, String endStop, int departureTime) {

        StopTime start = null;
        StopTime end = null;

        if (start == null || end == null) {
            return null;
        }

        routeFinder.findRoute(start, end, graph.getGraph());
        return new ArrayList<>();
    }

    public StopTime findStopTime(String stopName, int time){
        if (stopName == null){
            throw new NullPointerException("Stop name cannot be null");
        }

        if (!stopNameMap.containsKey(stopName)){
            return null;
        }

        String stopId = stopNameMap.get(stopName).getStopId();

        List<StopTime> stopTimes = stopMap.get(stopId);

        StopTime stopTime = null;

        for (StopTime s : stopTimes){
            if (s.getDepartureTime() > time){
                stopTime = s;
                break;
            }
        }

        return stopTime;
    } 

}

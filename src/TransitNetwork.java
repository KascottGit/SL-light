
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransitNetwork {

    private TransitGraph graph;

    private HashMap<String, List<StopTime>> tripMap;
    private HashMap<String, List<StopTime>> stopMap;

    private HashMap<String, Stop> stopDirectory;

    public TransitNetwork() {
        graph = new TransitGraph();
        tripMap = new HashMap<>();
        stopMap = new HashMap<>();
        stopDirectory = new HashMap<>();
    }

    public void buildGraph(List<StopTime> stopTimes, List<Stop> stops) {
        //Populate stopDirectory
        for (Stop stop : stops) {
            stopDirectory.put(stop.getStopId(), stop);
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

}

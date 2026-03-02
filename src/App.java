
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App {

    public static void main(String[] args) throws Exception {

        TransitGraph graph = new TransitGraph();

        GtfsDataLoader dataLoader = new GtfsDataLoader();

        List<StopTime> stopTimes = dataLoader.loadStopTimes("D:/Document/Skola/ALDA/sl_gtfs_data/sl_stop_times.txt");
        List<Stop> stops = dataLoader.loadStops("D:/Document/Skola/ALDA/sl_gtfs_data/stops.txt");

        HashMap<String, List<StopTime>> tripMap = new HashMap<>();
        HashMap<String, List<StopTime>> stopMap = new HashMap<>();

        HashMap<String, Stop> stopDirectory = new HashMap<>();

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

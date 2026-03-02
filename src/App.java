
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App {

    public static void main(String[] args) throws Exception {

        TransitGraph graph = new TransitGraph();

        GtfsDataLoader dataLoader = new GtfsDataLoader();

        List<StopTime> stopTimes = dataLoader.loadStopTimes("D:/Document/Skola/ALDA/sl_gtfs_data/sl_stop_times.txt");
        HashMap<String, List<StopTime>> tripMap = new HashMap<>();
        HashMap<String, List<StopTime>> stopMap = new HashMap<>();

        //Populate graph and maps
        for (StopTime stopTime : stopTimes) {
            //Add to graph
            graph.add(stopTime);

            //Map to tripId
            List<StopTime> tripList = tripMap.getOrDefault(stopTime.getTripId(), new ArrayList<>());
            tripList.add(stopTime);
            tripMap.put(stopTime.getTripId(), tripList);

            //Map to stopId
            List<StopTime> stopList = stopMap.getOrDefault(stopTime.getStopId(), new ArrayList<>());
            stopList.add(stopTime);
            stopMap.put(stopTime.getStopId(), stopList);
        }

        //Make connections to same trip forward
        for (Map.Entry<String, List<StopTime>> entry : tripMap.entrySet()) {
            List<StopTime> list = entry.getValue();
            list.sort((a, b) -> a.getStopSequence() - b.getStopSequence());

            for (int i = 0; i < list.size() - 1; i++) {
                StopTime stopTime = list.get(i);
                graph.connect(stopTime, list.get(i + 1));
            }
        }

        //Make connections to same stop forward in time
        for (Map.Entry<String, List<StopTime>> entry : stopMap.entrySet()) {
            List<StopTime> list = entry.getValue();
            list.sort((a, b) -> a.getDepartureTime() - b.getDepartureTime());

            for (int i = 0; i < list.size() - 1; i++) {
                StopTime stopTime = list.get(i);

                for (int j = i + 1; j < list.size(); j++) {
                    StopTime other = list.get(j);
                    if (!stopTime.getTripId().equals(other.getTripId()) && stopTime.getDepartureTime() < other.getDepartureTime()) {
                        graph.connect(stopTime, other);
                    }
                }
            }
        }

    }

}

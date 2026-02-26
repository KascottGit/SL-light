
import java.util.List;

public class App {

    public static void main(String[] args) throws Exception {

        TransitGraph graph = new TransitGraph();

        GtfsDataLoader dataLoader = new GtfsDataLoader();
        
        List<StopTime> stopTimes = dataLoader.loadStopTimes("D:/Document/Skola/ALDA/sl_gtfs_data/sl_stop_times.txt");

        for (StopTime stopTime : stopTimes) {
            graph.add(stopTime);
        }
    }

}

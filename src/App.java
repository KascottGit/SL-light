
import java.util.List;

public class App {

    public static void main(String[] args) throws Exception {

        //Load data
        GtfsDataLoader dataLoader = new GtfsDataLoader();
        List<StopTime> stopTimes = dataLoader.loadStopTimes("sl_gtfs_data/sl_stop_times.txt");
        List<Stop> stops = dataLoader.loadStops("sl_gtfs_data/sl_stops.txt");

        //Build graph
        TransitNetwork transitNetwork = new TransitNetwork();
        transitNetwork.buildGraph(stopTimes, stops);

        //Start UI
        TerminalUI ui = new TerminalUI(transitNetwork);
        ui.start();
    }

}

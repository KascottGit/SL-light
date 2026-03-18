
import java.util.*;

public class App {

    public static void main(String[] args) throws Exception {

        //Load data
        GtfsDataLoader dataLoader = new GtfsDataLoader();

        List<StopTime> stopTimes = dataLoader.loadStopTimes("sl_gtfs_data/sl_stop_times.txt");
        List<Stop> stops = dataLoader.loadStops("sl_gtfs_data/sl_stops.txt");
        List<Trip> trips = dataLoader.loadTrips("sl_gtfs_data/sl_trips.txt");
        List<Route> routes = dataLoader.loadRoutes("sl_gtfs_data/sl_routes.txt");

        //Build graph
        TransitNetwork transitNetwork = new TransitNetwork();
        transitNetwork.buildGraph(stopTimes, stops, trips, routes);

        //Start UI
        TerminalUI ui = new TerminalUI(transitNetwork);
        ui.start();
    }

}

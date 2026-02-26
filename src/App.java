
public class App {

    public static void main(String[] args) throws Exception {
        TransitGraph graph = new TransitGraph();
        GtfsDataLoader dataLoader = new GtfsDataLoader();

        dataLoader.loadStopTimes("D:/Document/Skola/ALDA/sl_gtfs_data/sl_stop_times.txt", graph);
        System.out.println(graph);
    }

}


public class TerminalUI {

    private TransitNetwork transitNetwork;
    private AStarRouteFinder routeFinder;

    private String fromStopName;
    private String toStopName;

    private int atTime;

    public TerminalUI(TransitNetwork transitNetwork) {
        this.transitNetwork = transitNetwork;
    }

    public void start() {
        routeFinder = new AStarRouteFinder();

        System.out.println("Welcome to SL");
        System.out.println("Commands: ");
        System.out.println("searchTrip");
        System.out.println("help");
    }

    private void findRoute() {
        //find stoptime based on stop

        StopTime fromStopTime = transitNetwork.findStopTime(fromStopName, atTime);
        StopTime toStopTime = transitNetwork.findStopTime(toStopName, atTime);

        if (fromStopTime == null) {
            //NOT FOUND!
        }

        if (toStopTime == null) {
            //NOT FOUND!
        }

        transitNetwork.findRoute(fromStopName, toStopName, atTime);
    }
}

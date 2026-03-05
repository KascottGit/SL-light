
public class TerminalUI {

    private TransitNetwork transitNetwork; 

    public TerminalUI(TransitNetwork transitNetwork){
        this.transitNetwork = transitNetwork;
    }
    public void start() {
        System.out.println("Welcome to SL");
        System.out.println("Commands: ");
        System.out.println("searchTrip");
        System.out.println("help");
    }
}


public class TerminalUI {

    private TransitNetwork transitNetwork; 

    private String fromStop;
    private String toStop;

    private int atTime; 


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

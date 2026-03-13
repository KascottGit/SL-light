
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class TerminalUI {

    private TransitNetwork transitNetwork;
    private AStarRouteFinder routeFinder;

    private String fromStopName;
    private String toStopName;

    private int atTime;
    private boolean isTimeNull;

    public TerminalUI(TransitNetwork transitNetwork) {
        this.transitNetwork = transitNetwork;
    }

    public void start() {
        routeFinder = new AStarRouteFinder();

        System.out.println("Welcome to SL");

        listCommands();

        readInput();
    }

    private void findRoute() {
        //find stoptime based on stop

        StopTime fromStopTime = transitNetwork.findStopTime(fromStopName, atTime);
        StopTime toStopTime = transitNetwork.findStopTime(toStopName, atTime);

        if (fromStopTime == null) {
            System.out.println("Error: " + fromStopName + " does not exist! ");
            fromStopName = null;
        }

        if (toStopTime == null) {
            System.out.println("Error: " + toStopName + " does not exist! ");
            toStopName = null;
        }

        if (fromStopTime == null || toStopTime == null) {
            return;
        }
        System.out.println("Finding route to " + toStopName + " from " + fromStopName + " at " + atTime);
        transitNetwork.findRoute(fromStopName, toStopName, atTime);
    }

    private void listCommands() {
        System.out.println("~~~~~Commands~~~~~");
        System.out.println("1. from <station name>");
        System.out.println("2. to <station name>");
        System.out.println("3. at <time of departure in hh:mm>");
        System.out.println("4. help");
        System.out.println("5. quit");
        System.out.println("");
    }

    private void readInput() {
        Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);
        isTimeNull = true;

        boolean hasQuit = false;

        while (true) {

            StringBuilder command = new StringBuilder();
            String[] words = scanner.nextLine().split(" ");

            for (int i = 0; i < words.length; i++) {
                switch (words[i].toLowerCase()) {
                    case "from":
                        for (int j = i; j < words.length; j++) {
                            if (j == i) {
                                continue;
                            }
                            if (isCommand(words[j])) {
                                break;
                            }
                            command.append(words[j]);
                            command.append(" ");
                        }
                        fromStopName = command.toString().trim();
                        command.setLength(0);
                        break;
                    case "to":
                        for (int j = i; j < words.length; j++) {
                            if (j == i) {
                                continue;
                            }
                            if (isCommand(words[j])) {
                                break;
                            }
                            command.append(words[j]);
                            command.append(" ");
                        }
                        toStopName = command.toString().trim();
                        command.setLength(0);
                        break;
                    case "at":
                        for (int j = i; j < words.length; j++) {
                            if (j == i) {
                                continue;
                            }
                            if (isCommand(words[j])) {
                                break;
                            }
                            command.append(words[j]);
                            command.append(" ");
                        }
                        atTime = parseTime(command.toString());

                        command.setLength(0);
                        break;
                    case "quit":
                        hasQuit = true;
                        break;
                    case "help":
                        listCommands();
                        break;
                    default:
                        break;
                }
            }

            if (hasQuit) {
                break;
            }

            if (fromStopName != null && toStopName != null && !isTimeNull) {
                findRoute();
            }

        }

        scanner.close();
    }

    private boolean isCommand(String string) {
        return string.equals("from") || string.equals("to")
                || string.equals("at") || string.equals("help") || string.equals("quit");
    }

    private int parseTime(String input) {

        input = input.trim();
        int length = input.length();

        if (length < 4 || length > 5) {
            isTimeNull = true;
            System.out.println("Error: Use time format hh:mm or h:mm");
            return -1;
        }
        if (input.charAt(length - 3) != ':') {
            isTimeNull = true;
            System.out.println("Error: Use time format hh:mm or h:mm");
            return -1;
        }
        isTimeNull = false;

        String[] time = input.split(":");

        int hours = Integer.parseInt(time[0]);
        int minutes = Integer.parseInt(time[1]);

        return hours * 60 + minutes;
    }

}


import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

public class TerminalUI {

    private TransitNetwork transitNetwork;

    private Stop fromStop;
    private Stop toStop;

    private int atTimeHours;
    private int atTimeMinutes;

    private boolean isTimeNull;

    public TerminalUI(TransitNetwork transitNetwork) {
        this.transitNetwork = transitNetwork;
    }

    public void start() {

        System.out.println("Welcome to SL");

        listCommands();

        readInput();
    }

    private void findRoute() {
        if (fromStop == null || toStop == null) {
            return;
        }
        System.out.println("Finding route to " + toStop.getName() + " from " + fromStop.getName() + " at " + atTimeHours + ":" + atTimeMinutes + "...");

        int atTime = atTimeHours * 60 + atTimeMinutes;

        List<StopTime> route = transitNetwork.findRoute(fromStop, toStop, atTime);

        printItinerary(route);
    }

    private void listCommands() {
        System.out.println("~~~~~ Commands ~~~~~");
        System.out.println("1. from <station name>");
        System.out.println("2. to <station name>");
        System.out.println("3. at <time of departure in hh:mm or h:mm>");
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
                        String fromStopName = command.toString().trim().toLowerCase();
                        fromStop = transitNetwork.findStopByName(fromStopName);
                        if (fromStop == null) {
                            System.out.println("Error: " + fromStopName + " does not exist! ");
                        }
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
                        String toStopName = command.toString().trim().toLowerCase();
                        toStop = transitNetwork.findStopByName(toStopName);
                        if (toStop == null) {
                            System.out.println("Error: " + toStopName + " does not exist! ");
                        }
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
                        parseTime(command.toString());

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

            if (fromStop != null && fromStop != null && !isTimeNull) {
                findRoute();
            }

        }

        scanner.close();
    }

    private boolean isCommand(String string) {
        return string.equals("from") || string.equals("to")
                || string.equals("at") || string.equals("help") || string.equals("quit");
    }

    private void parseTime(String input) {

        input = input.trim();
        int length = input.length();

        if (length < 4 || length > 5) {
            isTimeNull = true;
            System.out.println("Error: Use time format hh:mm or h:mm");
            return;
        }
        if (input.charAt(length - 3) != ':') {
            isTimeNull = true;
            System.out.println("Error: Use time format hh:mm or h:mm");
            return;
        }
        isTimeNull = false;

        String[] time = input.split(":");

        atTimeHours = Integer.parseInt(time[0]);
        atTimeMinutes = Integer.parseInt(time[1]);
    }

    private void printItinerary(List<StopTime> route) {
        if (route == null || route.isEmpty()) {
            System.out.println("Error: No valid route found.");
            return;
        }

        StopTime firstStop = route.get(0);
        StopTime lastStop = route.get(route.size() - 1);

        int totalMinutes = lastStop.getDepartureTime() - firstStop.getDepartureTime();
        String startName = transitNetwork.findStopById(firstStop.getStopId()).getName();
        String endName = transitNetwork.findStopById(lastStop.getStopId()).getName();

        // Print Summary Header
        System.out.println("\n-----------------------------\n");
        System.out.println(totalMinutes + " min");
        System.out.println(formatTime(firstStop.getDepartureTime()) + " -> " + formatTime(lastStop.getDepartureTime()));
        System.out.println(startName + " -> " + endName);
        System.out.println("\n-----------------------------\n");

        int i = 0;
        while (i < route.size() - 1) {
            StopTime current = route.get(i);
            StopTime next = route.get(i + 1);

            // 1. Wait Block: Same StopId
            if (current.getStopId().equals(next.getStopId())) {
                int waitStartIdx = i;
                // Fast-forward through all contiguous wait nodes at this stop
                while (i < route.size() - 1 && route.get(i).getStopId().equals(route.get(i + 1).getStopId())) {
                    i++;
                }
                int waitTime = route.get(i).getDepartureTime() - route.get(waitStartIdx).getDepartureTime();

                if (waitTime > 0) {
                    System.out.println("\n- Wait " + waitTime + " min -\n");
                }
            } // 2. Travel Block: Same TripId, Different StopId
            else if (current.getTripId().equals(next.getTripId())) {
                int travelStartIdx = i;
                // Fast-forward through all contiguous travel nodes on this trip
                while (i < route.size() - 1 && route.get(i).getTripId().equals(route.get(i + 1).getTripId())) {
                    i++;
                }

                StopTime travelStart = route.get(travelStartIdx);
                StopTime travelEnd = route.get(i);

                int travelTime = travelEnd.getDepartureTime() - travelStart.getDepartureTime();
                String legStartName = transitNetwork.findStopById(travelStart.getStopId()).getName();
                String legEndName = transitNetwork.findStopById(travelEnd.getStopId()).getName();

                System.out.println(formatTime(travelStart.getDepartureTime()) + " " + legStartName);
                System.out.println("|");
                System.out.println(travelTime + " min - Trip " + travelStart.getTripId());
                System.out.println("|");
                System.out.println(formatTime(travelEnd.getDepartureTime()) + " " + legEndName);
            } // 3. Unhandled Graph Edge (e.g., Footpaths)
            else {
                System.out.println("Error: Unhandled edge from Stop " + current.getStopId() + " to " + next.getStopId());
                i++;
            }
        }
        System.out.println();
    }

    private String formatTime(int minutes) {
        return String.format("%02d:%02d", minutes / 60, minutes % 60);
    }

}

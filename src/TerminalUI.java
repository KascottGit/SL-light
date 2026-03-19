
import java.util.*;

public class TerminalUI {

    private TransitNetwork transitNetwork;

    private Stop fromStop;
    private Stop toStop;

    private int departureTime;

    public TerminalUI(TransitNetwork transitNetwork) {
        this.transitNetwork = transitNetwork;
    }

    final public void start() {
        System.out.println("\n\n\n-------SL Route Finder-------");
        listCommands();
        readInput();
    }

    private void findRoute() {
        System.out.println("\nFinding route to " + toStop.getName() + " from " + fromStop.getName() + " at " + formatTime(departureTime) + "...");

        List<StopTime> route = transitNetwork.findRoute(fromStop, toStop, departureTime);
        route = trimInitialWait(route);
        printRoute(route);
    }

    private void listCommands() {
        System.out.println("\nCOMMANDS:");
        System.out.println("  from <station name>");
        System.out.println("  to <station name>");
        System.out.println("  at <time of departure in hh:mm or h:mm>");
        System.out.println("  help");
        System.out.println("  quit");
        System.out.println("");
    }

    private void readInput() {
        Scanner scanner = new Scanner(System.in);
        departureTime = -1;

        boolean hasQuit = false;

        while (true) {

            String[] words = scanner.nextLine().split(" ");
            String argument;

            for (int i = 0; i < words.length; i++) {
                switch (words[i].toLowerCase()) {
                    case "from":
                        argument = readArgument(words, i);
                        fromStop = transitNetwork.getStopByName(argument);
                        if (fromStop == null) {
                            System.out.println("Error: " + argument + " does not exist! ");
                        }
                        break;
                    case "to":
                        argument = readArgument(words, i);
                        toStop = transitNetwork.getStopByName(argument);
                        if (toStop == null) {
                            System.out.println("Error: " + argument + " does not exist! ");
                        }
                        break;
                    case "at":
                        argument = readArgument(words, i);
                        departureTime = parseTime(argument);
                        if (departureTime == -1) {
                            System.out.println("Error: Use time format hh:mm or h:mm");
                        }
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

            if (toStop != null && fromStop != null && departureTime != -1) {
                findRoute();
            }

        }
        scanner.close();
    }

    private boolean isCommand(String string) {
        return string.equals("from") || string.equals("to")
                || string.equals("at") || string.equals("help") || string.equals("quit");
    }

    private String readArgument(String[] words, int i) {
        StringBuilder argument = new StringBuilder();

        for (int j = i; j < words.length; j++) {
            if (j == i) {
                continue;
            }
            if (isCommand(words[j])) {
                break;
            }
            argument.append(words[j]);
            argument.append(" ");
        }

        return argument.toString().trim().toLowerCase();
    }

    private int parseTime(String input) {
        input = input.trim();
        int length = input.length();

        if (length < 4 || length > 5) {
            return -1;
        }
        if (input.charAt(length - 3) != ':') {
            return -1;
        }

        String[] time = input.split(":");
        int timeHours = Integer.parseInt(time[0]);
        int timeMinutes = Integer.parseInt(time[1]);

        if (timeHours > 24 || timeHours < 0 || timeMinutes > 60 || timeMinutes < 0) {
            return -1;
        }

        return timeHours * 60 + timeMinutes;
    }

    private void printRoute(List<StopTime> route) {
        if (route == null || route.isEmpty()) {
            System.out.println("Error: No route found!");
            return;
        }

        StopTime firstStop = route.get(0);
        StopTime lastStop = route.get(route.size() - 1);

        int totalMinutes = lastStop.getDepartureTime() - firstStop.getDepartureTime();
        String startName = transitNetwork.getStopById(firstStop.getStopId()).getName();
        String endName = transitNetwork.getStopById(lastStop.getStopId()).getName();

        // Header
        System.out.println("\n-----------------------------\n");
        System.out.println(totalMinutes + " min");
        System.out.println(formatTime(firstStop.getDepartureTime()) + " -> " + formatTime(lastStop.getDepartureTime()));
        System.out.println(startName + " -> " + endName);
        System.out.println("\n-----------------------------\n");

        int i = 0;
        while (i < route.size() - 1) {
            StopTime current = route.get(i);
            StopTime next = route.get(i + 1);

            // 1. Wait
            if (current.getStopId().equals(next.getStopId())) {
                int waitStartIdx = i;
                while (i < route.size() - 1 && route.get(i).getStopId().equals(route.get(i + 1).getStopId())) {
                    i++;
                }
                int waitTime = route.get(i).getDepartureTime() - route.get(waitStartIdx).getDepartureTime();

                if (waitTime > 0) {
                    System.out.println("\n- Wait " + waitTime + " min -\n");
                }
            } // 2. Travel
            else if (current.getTripId().equals(next.getTripId())) {
                int travelStartIndex = i;

                while (i < route.size() - 1 && route.get(i).getTripId().equals(route.get(i + 1).getTripId())) {
                    i++;
                }

                StopTime travelStart = route.get(travelStartIndex);
                StopTime travelEnd = route.get(i);

                int travelTime = travelEnd.getDepartureTime() - travelStart.getDepartureTime();
                String legStartName = transitNetwork.getStopById(travelStart.getStopId()).getName();
                String legEndName = transitNetwork.getStopById(travelEnd.getStopId()).getName();

                System.out.println(formatTime(travelStart.getDepartureTime()) + " " + legStartName);
                System.out.println("|");
                System.out.println(travelTime + " min - " + (i - travelStartIndex) + " stops along " + transitNetwork.getTripInformation(travelStart.getTripId()));
                System.out.println("|");
                System.out.println(formatTime(travelEnd.getDepartureTime()) + " " + legEndName);
            } else {
                i++;
            }
        }
        System.out.println();
    }

    private List<StopTime> trimInitialWait(List<StopTime> route) {
        if (route == null || route.size() < 2) {
            return route;
        }

        int boardIndex = 0;
        for (int i = 0; i < route.size() - 1; i++) {
            StopTime current = route.get(i);
            StopTime next = route.get(i + 1);

            if (current.getTripId().equals(next.getTripId())) {
                boardIndex = i;
                break;
            }
        }

        return new ArrayList<>(route.subList(boardIndex, route.size()));
    }

    private String formatTime(int minutes) {
        return String.format("%02d:%02d", minutes / 60, minutes % 60);
    }

}

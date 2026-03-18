
import java.util.*;

public class AStarRouteFinder {

    private final TransitNetwork network;

    public AStarRouteFinder(TransitNetwork network) {
        this.network = network;
    }

    public List<StopTime> findRoute(StopTime start, Stop end, Map<StopTime, Map<StopTime, Integer>> graph) {
        if (start == null || end == null || !graph.containsKey(start)) {
            return Collections.emptyList();
        }

        PriorityQueue<RouteNode> openSet = new PriorityQueue<>();
        Map<StateKey, Integer> gScore = new HashMap<>();

        // Start state
        Set<String> initialVisited = new HashSet<>();
        initialVisited.add(start.getStopId());

        RouteNode startNode = new RouteNode(
                start, 0, heuristic(start, end), start.getDepartureTime(), start.getTripId(), initialVisited, null
        );

        StateKey startKey = new StateKey(start, start.getTripId());
        gScore.put(startKey, 0);
        openSet.add(startNode);

        while (!openSet.isEmpty()) {
            RouteNode current = openSet.poll();
            StopTime currentStop = current.stopTime;

            // Goal reached
            if (currentStop.getStopId().equals(end.getStopId())) {
                return reconstructPath(current);
            }

            Map<StopTime, Integer> neighbors = graph.get(currentStop);
            if (neighbors == null) {
                continue;
            }

            for (Map.Entry<StopTime, Integer> neighborEntry : neighbors.entrySet()) {
                StopTime neighbor = neighborEntry.getKey();
                int edgeCost = neighborEntry.getValue();

                boolean isSameTrip = currentStop.getTripId().equals(neighbor.getTripId());
                boolean isSameStop = currentStop.getStopId().equals(neighbor.getStopId());

                if (isSameStop && !isSameTrip) {
                    if (current.arrivalTripId.equals(currentStop.getTripId())) {
                        if (currentStop.getStopType() == 2) {
                            continue;
                        }
                    }
                }

                if (isSameTrip && !current.arrivalTripId.equals(currentStop.getTripId())) {
                    if (currentStop.getStopType() == 0) {
                        continue;
                    }

                    if (currentStop.getDepartureTime() - current.stationArrivalTime < 1) {
                        continue;
                    }
                }

                if (!isSameStop) {
                    if (current.visitedStops.contains(neighbor.getStopId())) {
                        continue;
                    }
                }

                int tentativeGScore = current.gScore + edgeCost;

                int nextArrivalTime = isSameTrip ? neighbor.getDepartureTime() : current.stationArrivalTime;
                String nextArrivalTripId = isSameTrip ? neighbor.getTripId() : current.arrivalTripId;

                StateKey neighborKey = new StateKey(neighbor, nextArrivalTripId);

                if (tentativeGScore < gScore.getOrDefault(neighborKey, Integer.MAX_VALUE)) {
                    gScore.put(neighborKey, tentativeGScore);

                    int fScore = tentativeGScore + heuristic(neighbor, end);

                    Set<String> nextVisited = new HashSet<>(current.visitedStops);
                    if (!isSameStop) {
                        nextVisited.add(neighbor.getStopId());
                    }

                    openSet.add(new RouteNode(neighbor, tentativeGScore, fScore, nextArrivalTime, nextArrivalTripId, nextVisited, current));
                }
            }
        }

        return Collections.emptyList(); // No valid path exists
    }

    private int heuristic(StopTime current, Stop end) {
        double distanceKm = network.distanceBetweenStops(current.getStopId(), end.getStopId());
        if (distanceKm < 0) {
            return 0;
        }
        return (int) (distanceKm / 2);
    }

    private List<StopTime> reconstructPath(RouteNode endNode) {
        List<StopTime> path = new ArrayList<>();
        RouteNode current = endNode;
        while (current != null) {
            path.add(0, current.stopTime);
            current = current.parent;
        }
        return path;
    }

    private static class StateKey {

        private final StopTime stopTime;
        private final String arrivalTripId;

        public StateKey(StopTime stopTime, String arrivalTripId) {
            this.stopTime = stopTime;
            this.arrivalTripId = arrivalTripId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            StateKey stateKey = (StateKey) o;
            return stopTime.equals(stateKey.stopTime) && arrivalTripId.equals(stateKey.arrivalTripId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(stopTime, arrivalTripId);
        }
    }

    private static class RouteNode implements Comparable<RouteNode> {

        final StopTime stopTime;
        final int gScore;
        final int fScore;

        final int stationArrivalTime;
        final String arrivalTripId;
        final Set<String> visitedStops;

        final RouteNode parent;

        RouteNode(StopTime stopTime, int gScore, int fScore, int stationArrivalTime, String arrivalTripId, Set<String> visitedStops, RouteNode parent) {
            this.stopTime = stopTime;
            this.gScore = gScore;
            this.fScore = fScore;
            this.stationArrivalTime = stationArrivalTime;
            this.arrivalTripId = arrivalTripId;
            this.visitedStops = visitedStops;
            this.parent = parent;
        }

        @Override
        public int compareTo(RouteNode other) {
            return fScore - other.fScore;
        }
    }
}

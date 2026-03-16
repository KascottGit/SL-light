
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class AStarRouteFinder {

    private TransitNetwork transitNetwork;

    private final TransitNetwork network;

    // Inject the network so the algorithm can calculate the spatial heuristic
    public AStarRouteFinder(TransitNetwork network) {
        this.network = network;
    }

    public List<StopTime> findRoute(StopTime start, Stop end, Map<StopTime, Map<StopTime, Integer>> graph) {
        if (start == null || end == null || !graph.containsKey(start)) {
            return Collections.emptyList();
        }

        PriorityQueue<RouteNode> openSet = new PriorityQueue<>();
        Map<StopTime, Integer> gScore = new HashMap<>();
        Map<StopTime, StopTime> cameFrom = new HashMap<>();

        gScore.put(start, 0);
        openSet.add(new RouteNode(start, 0, heuristic(start, end)));

        while (!openSet.isEmpty()) {
            RouteNode current = openSet.poll();
            StopTime currentStop = current.stopTime;

            // Goal reached
            if (currentStop.getStopId().equals(end.getStopId())) {
                return reconstructPath(cameFrom, currentStop);
            }

            Map<StopTime, Integer> neighbors = graph.get(currentStop);
            if (neighbors == null) {
                continue;
            }

            for (Map.Entry<StopTime, Integer> neighborEntry : neighbors.entrySet()) {
                StopTime neighbor = neighborEntry.getKey();
                int edgeCost = neighborEntry.getValue();

                int tentativeGScore = gScore.getOrDefault(currentStop, Integer.MAX_VALUE) + edgeCost;

                if (tentativeGScore < gScore.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    cameFrom.put(neighbor, currentStop);
                    gScore.put(neighbor, tentativeGScore);

                    int fScore = tentativeGScore + heuristic(neighbor, end);
                    openSet.add(new RouteNode(neighbor, tentativeGScore, fScore));
                }
            }
        }

        return Collections.emptyList(); // No path found
    }

    private int heuristic(StopTime current, Stop end) {
        double distanceKm = network.distanceBetweenStops(current.getStopId(), end.getStopId());
        if (distanceKm < 0) {
            return 0;
        }
        return (int) (distanceKm / 2);
    }

    private List<StopTime> reconstructPath(Map<StopTime, StopTime> cameFrom, StopTime current) {
        List<StopTime> path = new ArrayList<>();
        path.add(current);
        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            path.add(0, current); // Prepend to reverse the path automatically
        }
        return path;
    }

    private static class RouteNode implements Comparable<RouteNode> {

        StopTime stopTime;
        int gScore;
        int fScore;

        RouteNode(StopTime stopTime, int gScore, int fScore) {
            this.stopTime = stopTime;
            this.gScore = gScore;
            this.fScore = fScore;
        }

        @Override
        public int compareTo(RouteNode other) {
            return Integer.compare(this.fScore, other.fScore);
        }
    }
}

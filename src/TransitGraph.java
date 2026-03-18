
import java.util.*;

public class TransitGraph {

    private final Map<StopTime, Map<StopTime, Integer>> graph;

    public TransitGraph() {
        graph = new HashMap<>();
    }

    public boolean add(StopTime newStopTime) {
        if (graph.containsKey(newStopTime)) {
            return false;
        }
        graph.put(newStopTime, new HashMap<>());
        return true;
    }

    public boolean connect(StopTime from, StopTime to) {
        if (!(graph.containsKey(from) && graph.containsKey(to))) {
            return false;
        }
        int cost = to.getDepartureTime() - from.getDepartureTime();
        graph.get(from).put(to, cost);

        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (StopTime st : graph.keySet()) {
            sb.append(st);
            sb.append(", ");
        }
        return sb.toString();
    }

    public Map<StopTime, Map<StopTime, Integer>> getGraph() {
        return graph;
    }
}

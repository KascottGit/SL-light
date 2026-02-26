

public class App {

    public static void main(String[] args) throws Exception {
        TransitGraph graph = new TransitGraph();

        StopTime stop1 = new StopTime(1, 1, 1, 1, 1);
        StopTime stop2 = new StopTime(2, 2, 1, 1, 2);

        graph.add(stop1);
        graph.add(stop2);

        graph.connect(stop1, stop2);

    }

    
}

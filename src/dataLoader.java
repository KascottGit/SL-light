
import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class DataLoader {

    public void loadStopTimes(String filePath, TransitGraph graph) {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath))) {
            String header = reader.readLine();
            Map<String, Integer> colMap = mapHeaders(header);

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                int stopId = Integer.parseInt(parts[colMap.get("stop_id")]);
                int departureTime = parseTimeToMinutes(parts[colMap.get("departure_time")]);
                int stopType = parseStopType(parts[colMap.get("pickup_type")], parts[colMap.get("drop_off_type")]);
                int tripId = Integer.parseInt(parts[colMap.get("trip_id")]);
                int stopSequence = Integer.parseInt(parts[colMap.get("stop_sequence")]);

                graph.add(new StopTime(stopId, departureTime, stopType, tripId, stopSequence));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Map<String, Integer> mapHeaders(String header) {
        Map<String, Integer> colMap = new HashMap<>();
        String[] columns = header.split(",");

        for (int i = 0; i < columns.length; i++) {
            colMap.put(columns[i].trim(), i);
        }
        return colMap;
    }

    private int parseTimeToMinutes(String gtfsTime) {
        String[] hms = gtfsTime.trim().split(":");
        return Integer.parseInt(hms[0]) * 60
                + Integer.parseInt(hms[1]);
    }

    private int parseStopType(String pickupType, String dropOffType) {
        if (pickupType.equals("1")) {
            return 2;
        }
        if (dropOffType.equals("1")) {
            return 1;
        }
        return 0;
    }
}

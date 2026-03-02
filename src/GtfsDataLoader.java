
import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GtfsDataLoader {

    public List<StopTime> loadStopTimes(String filePath) {
        List<StopTime> stopTimes = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath))) {
            String header = reader.readLine();
            Map<String, Integer> colMap = mapHeaders(header);

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                String stopId = parts[colMap.get("stop_id")];
                int departureTime = parseTimeToMinutes(parts[colMap.get("departure_time")]);
                int stopType = parseStopType(parts[colMap.get("pickup_type")], parts[colMap.get("drop_off_type")]);
                String tripId = parts[colMap.get("trip_id")];
                int stopSequence = Integer.parseInt(parts[colMap.get("stop_sequence")]);

                stopTimes.add(new StopTime(stopId, departureTime, stopType, tripId, stopSequence));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stopTimes;
    }

    public List<Stop> loadStops(String filePath) {
        List<Stop> stops = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath))) {
            String header = reader.readLine();
            Map<String, Integer> colMap = mapHeaders(header);

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                String stopId = parts[colMap.get("stop_id")];
                String name = parts[colMap.get("stop_name")];

                float posLat = Float.parseFloat(parts[colMap.get("stop_lat")]);
                float posLong = Float.parseFloat(parts[colMap.get("stop_lon")]);

                stops.add(new Stop(stopId, name, posLat, posLong));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stops;
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
            return 0;
        }
        return 1;
    }
}

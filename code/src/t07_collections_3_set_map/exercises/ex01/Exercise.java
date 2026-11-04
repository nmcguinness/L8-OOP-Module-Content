package t07_collections_3_set_map.exercises.ex01;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Exercise {

    // Counts distinct IP addresses in entries of the form "timestamp,ipAddress"
    public static int countUniqueVisitors(List<String> logEntries) {
        if (logEntries == null)
            throw new IllegalArgumentException("logEntries must not be null");

        Set<String> ips = new HashSet<>();
        for (String entry : logEntries) {
            if (entry == null)
                continue;

            int comma = entry.indexOf(',');
            if (comma < 0)
                continue;

            String ip = entry.substring(comma + 1).trim();
            if (!ip.isEmpty())
                ips.add(ip);
        }
        return ips.size();
    }

    public static void run() {
        List<String> log = List.of(
                "09:00,10.0.0.1",
                "09:01,10.0.0.2",
                "09:02,10.0.0.1",
                "malformed line",
                "09:03,10.0.0.3");
        System.out.println("Unique visitors: " + countUniqueVisitors(log));
    }
}

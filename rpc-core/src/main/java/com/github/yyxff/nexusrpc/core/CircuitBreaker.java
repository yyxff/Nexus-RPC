package com.github.yyxff.nexusrpc.core;

import java.net.InetSocketAddress;
import java.util.*;

// TODO: concurrent
// TODO: specified to service
public class CircuitBreaker {

    // Mapping form server to available time
    private final Map<InetSocketAddress, Long> availableTime = new HashMap();

    // Mapping from server to recent success record (5 recently records)
    private final Map<InetSocketAddress, ArrayList<Integer>> recentSuccess = new HashMap();

    // If success ratio < 50% success, open circuit(ban) this server
    private final double threshold = 0.5;

    // If banned, the server will be banned for 5000ms
    private final long openTimeout = 5000;

    /**
     * Get filter serverList (Some of them may be banned by circuit breaker)
     * @param serverList
     * @return
     */
    public Collection<InetSocketAddress> filter(Collection<InetSocketAddress> serverList) {
        Collection<InetSocketAddress> result = new HashSet<>();
        for (InetSocketAddress server : serverList) {
            if (!availableTime.containsKey(server)) {
                availableTime.put(server, System.currentTimeMillis());
                recentSuccess.put(server, new ArrayList<>(Arrays.asList(1, 1, 1, 1, 1)));
            }
            if (System.currentTimeMillis() >= availableTime.get(server)) {
                result.add(server);
            }
        }
        return result;
    }


    /**
     * Record one success
     * @param server
     */
    public void success(InetSocketAddress server) {
        ArrayList<Integer> recent = recentSuccess.get(server);
        recent.remove(0);
        recent.add(1);
    }

    /**
     * Record one failure
     * If fail too much recently, ban this server for some time
     * @param server
     */
    public void fail(InetSocketAddress server) {
        ArrayList<Integer> recent = recentSuccess.get(server);
        recent.remove(0);
        recent.add(0);
        double average = recent.stream()
                               .mapToInt(Integer::intValue)
                               .average()
                               .orElse(0.0);
        // If fail too much recently, ban this server for some time
        if (System.currentTimeMillis() < availableTime.get(server)
            && average < threshold) {
            availableTime.put(server, System.currentTimeMillis() + openTimeout);
        }
    }
}

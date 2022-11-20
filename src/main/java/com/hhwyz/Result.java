package com.hhwyz;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author erniu.wzh
 * @date 2022/11/15 14:41
 */
public class Result {
    static Queue<Server> serverList = new ConcurrentLinkedQueue<>();

    public static void add(Server server) {
        serverList.add(server);
        pushToTable();
    }

    private synchronized static void pushToTable() {
        try {
            Map<Server, List<Long>> serverListMap = new HashMap<>();
            for (Server server : serverList) {
                if (serverListMap.containsKey(server)) {
                    serverListMap.get(server).add(server.getTime());
                } else {
                    serverListMap.put(server, Stream.of(server.getTime()).collect(Collectors.toList()));
                }
            }
            List<List<Object>> result = new ArrayList<>();
            for (Map.Entry<Server, List<Long>> serverListEntry : serverListMap.entrySet()) {
                if (serverListEntry.getValue().stream().anyMatch(Objects::nonNull)) {
                    DescriptiveStatistics stats = new DescriptiveStatistics();
                    serverListEntry.getValue().stream().filter(Objects::nonNull).forEach(tv -> stats.addValue((double) tv));
                    List<Object> list = Stream.of(
                            serverListEntry.getKey().getServerAddress(),
                            String.format("%.0f", stats.getMean()),
                            String.format("%.0f", stats.getStandardDeviation()),
                            String.format("%.0f%%", (double) serverListEntry.getValue().stream().filter(Objects::nonNull).count() / serverListEntry.getValue().size() * 100),
                            serverListEntry.getValue().size(),
                            serverListEntry.getValue()).collect(Collectors.toList());

                    result.add(list);
                }
            }
            List<List<Object>> listSorted = result.stream().sorted((i1, i2) -> Double.compare(Double.parseDouble((String) i1.get(1)), Double.parseDouble((String) i2.get(1)))).collect(Collectors.toList());

            FaceTableModel.data = new String[listSorted.size()][6];

            int row = 0;
            for (List<Object> objects : listSorted) {
                for (int i = 0; i < objects.size(); i++) {
                    Face.ftm.setValueAt(objects.get(i), row, i);
                }
                row++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

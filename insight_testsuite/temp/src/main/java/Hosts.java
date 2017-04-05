import java.io.*;
import java.util.*;

public class Hosts {

    private Map<String, HostNode> hostsMap;
    private Queue<HostNode> hostsQueue;
    private int topX;

    public Hosts(int topX) {
        this.topX = topX;
        hostsMap = new HashMap<>();
        hostsQueue = new PriorityQueue<>(new Comparator<HostNode>() {
            @Override
            public int compare(HostNode hostNode, HostNode t1) {
                if (hostNode.getValue() > t1.getValue()) {
                    return 1;
                }

                if (hostNode.getValue() < t1.getValue()) {
                    return -1;
                }
                return 0;
            }
        });
    }

    public void processNextLine(String line) {

        String hostname;
        hostname = line.substring(0, line.indexOf(" "));
        if (hostsMap.containsKey(hostname)) {
            hostsMap.get(hostname).incrementValue();
        } else {
            hostsMap.put(hostname, new HostNode(hostname, 1));
        }
    }

    public void outputResults(String outputFile) throws IOException {

        hostsMap.forEach((k, v) -> {
            if (hostsQueue.size() < topX) {
                hostsQueue.add(v);
            } else {
                if (hostsQueue.peek().getValue() < v.getValue()) {
                    hostsQueue.remove();
                    hostsQueue.add(v);
                }
            }
        });

        StringBuilder fileStringBuilder = new StringBuilder();

        while (hostsQueue.size() > 0) {

            HostNode smallest = hostsQueue.remove();
            StringBuilder lineStringBuilder = new StringBuilder();

            if (hostsQueue.size() > 0) {
                lineStringBuilder.append("\n");
            }

            lineStringBuilder.append(smallest.getHost());
            lineStringBuilder.append(",");
            lineStringBuilder.append(smallest.getValue());
            fileStringBuilder.insert(0, lineStringBuilder);
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        writer.append(fileStringBuilder);
        writer.close();
    }

}
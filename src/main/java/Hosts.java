import java.io.*;
import java.util.*;

/*
*
 * Created by jhunter on 3/31/17.
 * First we parse in N lines contained in the file one line at a time, collecting hosts/ip names into a hashmap.
 *
 * We then take a pass through the hashmap, storing the top M hosts in a priority queue.
 *
 * By peeking at the root of the pq, we know the minimum of the top 10 requesting hosts so far. If we find a larger host
 * request value than the current minimum of the top 10 requesting hosts, we remove the root of the pq, and add the new
 * host and value to the pq.
 *
 * add() in a pq is an O(log(n)) operation, and peek()/remove() is an 0(log(1)) operation. This will give us the ability
 * to scale our solution to the top million or even billion requesting hosts, with only a log(n)
 * negative effect time performance.
*/
public class Hosts {

    Map<String, HostNode> hostsMap;
    Queue<HostNode> hostsQueue;

    public void processNextLine(String line) {

    }

    public void outputResults(String outputFile) {

    }

    public Hosts(String fileString, int topX) {

        try (BufferedReader br = new BufferedReader(new FileReader(fileString))) {
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

            String line;
            String hostname;
            while ((line = br.readLine()) != null) {
                hostname = line.substring(0, line.indexOf(" "));
                if (hostsMap.containsKey(hostname)) {
                    hostsMap.get(hostname).incrementValue();
                } else {
                    hostsMap.put(hostname, new HostNode(hostname, 1));
                }
            }

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


            BufferedWriter writer = new BufferedWriter(new FileWriter("log_output/hosts.txt"));
            writer.append(fileStringBuilder);
            writer.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
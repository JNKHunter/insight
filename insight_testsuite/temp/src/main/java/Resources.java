import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Resources {

    private Map<String, ResourceNode> resourceMap;
    private Queue<ResourceNode> resourceQueue;
    private int topX;
    private String logPattern;
    private Pattern pattern;
    Matcher matcher;

    public Resources(int topX) {
        this.topX = topX;
        logPattern = "[\\w.]+ - - \\[[/\\w\\S\\s]+] \"\\w+ ([/\\w\\S]+)[\\w\\S\\s]*\" [0-9]+ ([0-9\\-]+)";
        pattern = Pattern.compile(logPattern);

        resourceMap = new HashMap<>();
        resourceQueue = new PriorityQueue<>(new Comparator<ResourceNode>() {
            @Override
            public int compare(ResourceNode resourceNode, ResourceNode t1) {
                if (resourceNode.getBytes() > t1.getBytes()) {
                    return 1;
                }

                if (resourceNode.getBytes() < t1.getBytes()) {
                    return -1;
                }
                return 0;
            }
        });
    }

    public void processNextLine(String line) {
        long bytes = 0;
        matcher = pattern.matcher(line);

        if (matcher.find()){

            if(!matcher.group(2).equals("-")) {
                bytes = Long.parseLong(matcher.group(2));
            } else {
                bytes = 0;
            }

            if (resourceMap.containsKey(matcher.group(1))) {
                resourceMap.get(matcher.group(1)).incrementBytesBy(bytes);
            } else {
                resourceMap.put(matcher.group(1), new ResourceNode(matcher.group(1), bytes));
            }
        }
    }

    public void outputResults(String outputFile) throws IOException {
        resourceMap.forEach((k, v) -> {
            if (resourceQueue.size() < topX) {
                resourceQueue.add(v);
            } else {
                if (resourceQueue.peek().getBytes() < v.getBytes()) {
                    resourceQueue.remove();
                    resourceQueue.add(v);
                }
            }
        });

        StringBuilder fileStringBuilder = new StringBuilder();

        while (resourceQueue.size() > 0) {

            ResourceNode smallest = resourceQueue.remove();
            StringBuilder lineStringBuilder = new StringBuilder();

            if (resourceQueue.size() > 0) {
                lineStringBuilder.append("\n");
            }

            lineStringBuilder.append(smallest.getResource());
            fileStringBuilder.insert(0, lineStringBuilder);
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        writer.append(fileStringBuilder);
        writer.close();
    }
}
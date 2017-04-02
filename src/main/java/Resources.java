import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jhunter on 4/1/17.
 */
public class Resources {

    public Resources(String fileString, int topX) {

        try (BufferedReader br = new BufferedReader(new FileReader(fileString))) {

            String line;
            Map<String, ResourceNode> resourceMap = new HashMap<>();
            Queue<ResourceNode> resourceQueue = new PriorityQueue<>(new Comparator<ResourceNode>() {
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

            String logPattern = "[\\w.]+ - - \\[[/\\w\\S\\s]+] \"\\w+ ([/\\w\\S]+)[\\w\\S\\s]*\" [0-9]+ ([0-9\\-]+)";
            Pattern p = Pattern.compile(logPattern);

            long bytes = 0;
            while ((line = br.readLine()) != null) {

                Matcher matcher = p.matcher(line);

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


            BufferedWriter writer = new BufferedWriter(new FileWriter("log_output/resources.txt"));
            writer.append(fileStringBuilder);
            writer.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        Resources resources = new Resources("log_input/log.txt", 10);
    }
}

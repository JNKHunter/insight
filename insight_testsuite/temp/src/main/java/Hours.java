import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jhunter on 4/2/17.
 */
public class Hours {
    private List<LocalDateTime> dates;
    private Queue<HoursNode> datesQueue;
    private String logPattern;
    private Pattern pattern;
    private Matcher matcher;
    private DateTimeFormatter formatter;
    private int topX;

    public Hours(int topX) {
        this.topX = topX;
        logPattern = "[\\w.]+ - - \\[([/\\w\\S\\s]+) -[0-9]+] \"\\w+ [/\\w\\S]+[\\w\\S\\s]*\" [0-9]+ [0-9\\-]+";
        pattern = Pattern.compile(logPattern);
        formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss");

        dates = new ArrayList<>();
        datesQueue = new PriorityQueue<>(new Comparator<HoursNode>() {
            @Override
            public int compare(HoursNode hoursNode, HoursNode t1) {
                if (hoursNode.getNumEvents() > t1.getNumEvents()) {
                    return 1;
                }

                if (hoursNode.getNumEvents() < t1.getNumEvents()) {
                    return -1;
                }

                return 0;
            }
        });
    }

    public void processNextLine(String line) {
        matcher = pattern.matcher(line);

        if (matcher.find()) {
            dates.add(LocalDateTime.parse(matcher.group(1), formatter));
        }
    }

    public void outputResults(String outputFile) throws IOException {

        //The array is already sorted by time ascending.
        int rightPointer = 1;
        LocalDateTime oneHourFromNow;
        //Used to skip to the next second to keep the data a little more useful and insightful
        for (int i = 0; i < dates.size(); i++) {

            oneHourFromNow = dates.get(i).plusHours(1);

            while(dates.get(rightPointer).isBefore(oneHourFromNow) && rightPointer < (dates.size() - 1)){
                rightPointer += 1;
            }

            HoursNode newNode = new HoursNode(dates.get(i), (rightPointer - i) + 1);

            if(datesQueue.size() < topX){
                datesQueue.add(newNode);
            }else{
                if(datesQueue.peek().getNumEvents() < newNode.getNumEvents()) {
                    datesQueue.remove();
                    datesQueue.add(newNode);
                }
            }
        }

        StringBuilder fileStringBuilder = new StringBuilder();

        while (datesQueue.size() > 0) {

            HoursNode smallest = datesQueue.remove();
            StringBuilder lineStringBuilder = new StringBuilder();

            if (datesQueue.size() > 0) {
                lineStringBuilder.append("\n");
            }

            lineStringBuilder.append(smallest.getTime().format(DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss")));
            lineStringBuilder.append(" -0400,");
            lineStringBuilder.append(smallest.getNumEvents());
            fileStringBuilder.insert(0, lineStringBuilder);
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        writer.append(fileStringBuilder);
        writer.close();
    }
}
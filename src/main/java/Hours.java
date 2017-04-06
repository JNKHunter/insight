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

                if (hoursNode.getNumEvents() == t1.getNumEvents()) {
                    if (hoursNode.getTime().isBefore(t1.getTime())) {
                        return 1;
                    } else if (hoursNode.getTime().isAfter(t1.getTime())) {
                        return -1;
                    }
                }

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
        int leftPointer = 0;
        int rightPointer;
        int eventsFound;
        LocalDateTime secondsPointer = dates.get(0);
        LocalDateTime oneHourFromSecondsPointer = dates.get(0).plusHours(1);
        LocalDateTime endTimePointer = dates.get(dates.size() - 1);


        while(secondsPointer.isBefore(endTimePointer)) {

            /**
             * Find the next matching event
             * We could use a binary search here to save a few lookups, but keeping track of the left pointer
             * gets us pretty close.
             **/
            for(int i = leftPointer; i < dates.size(); i++) {
                leftPointer = i;
                if(dates.get(i).isAfter(secondsPointer) || dates.get(i).isEqual(secondsPointer)) break;
            }

            rightPointer = leftPointer;
            eventsFound = 0;

            //Start counting events.
            while (rightPointer < dates.size()) {
                if(dates.get(rightPointer).isBefore(oneHourFromSecondsPointer)) {
                    eventsFound += 1;
                    rightPointer += 1;
                } else {
                    break;
                }
            }

            HoursNode newNode = new HoursNode(secondsPointer, eventsFound);

            if(datesQueue.size() < topX){
                datesQueue.add(newNode);
            }else{
                if(datesQueue.peek().getNumEvents() < newNode.getNumEvents()) {
                    datesQueue.remove();
                    datesQueue.add(newNode);
                }
            }

            secondsPointer = secondsPointer.plusSeconds(1);
            oneHourFromSecondsPointer = secondsPointer.plusHours(1);
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
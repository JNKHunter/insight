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

    public Hours(String fileString, int topX) {
        //Loop through line, add each request to a max priority queue
        // TODO Tweak to run on the second
        try (BufferedReader br = new BufferedReader(new FileReader(fileString))) {
            List<LocalDateTime> dates = new ArrayList<>();
            Queue<HoursNode> datesQueue = new PriorityQueue<>(new Comparator<HoursNode>() {
                @Override
                public int compare(HoursNode hoursNode, HoursNode t1) {
                    if(hoursNode.getNumEvents() > t1.getNumEvents()) {
                        return 1;
                    }

                    if(hoursNode.getNumEvents() < t1.getNumEvents()) {
                        return -1;
                    }

                    return 0;
                }
            });

            String line;
            String logPattern = "[\\w.]+ - - \\[([/\\w\\S\\s]+) -[0-9]+] \"\\w+ [/\\w\\S]+[\\w\\S\\s]*\" [0-9]+ [0-9\\-]+";
            Pattern p = Pattern.compile(logPattern);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss");

            while ((line = br.readLine()) != null) {

                Matcher matcher = p.matcher(line);

                if (matcher.find()) {
                    dates.add(LocalDateTime.parse(matcher.group(1), formatter));
                }
            }

            //The array is already sorted by time ascending. For each second between the start date and the end date. loop through
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
                lineStringBuilder.append(" -400,");
                lineStringBuilder.append(smallest.getNumEvents());
                fileStringBuilder.insert(0, lineStringBuilder);
            }


            BufferedWriter writer = new BufferedWriter(new FileWriter("log_output/hours.txt"));
            writer.append(fileStringBuilder);
            writer.close();

        } catch(FileNotFoundException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}
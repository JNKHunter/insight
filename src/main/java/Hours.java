import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
            for (int i = 0; i < dates.size(); i++) {

                oneHourFromNow = dates.get(i).plusHours(1);

                while(dates.get(i+rightPointer).isBefore(oneHourFromNow)){
                    rightPointer += 1;
                }

                HoursNode newNode = new HoursNode(dates.get(1), (rightPointer - i) + 1);

                if(datesQueue.size() < topX){
                    datesQueue.add(newNode);
                }else{
                    if(datesQueue.peek().getNumEvents() < newNode.getNumEvents()) {
                        datesQueue.remove();
                        datesQueue.add(newNode);
                    }
                }

            }

        } catch(FileNotFoundException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Hours hours = new Hours("log_input/log.txt", 10);
    }
}
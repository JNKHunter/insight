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
            Queue<LocalDateTime> datesQueue = new PriorityQueue<>();

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



            }

            //Find the start month
            //Find the end month
            //Check every second starting from the first event

            /*Remove root of PQ, then peek at new root. if new root is within the hour, increment counter. Remove new root,
        and peek at next new root. Repeat process. once new root is not within the hour of original root, we've got our
        count for that hour. Push items back on the queue.
        */
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
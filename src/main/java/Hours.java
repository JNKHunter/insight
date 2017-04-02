import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Created by jhunter on 4/2/17.
 */
public class Hours {

    public Hours(String fileString, int topX) {

        //Loop through line, add each request to a max priority queue

        try (BufferedReader br = new BufferedReader(new FileReader(fileString))) {
            Queue<Date> dateQueue = new PriorityQueue<>(Collections.reverseOrder());

            String line;
            while ((line == br.readLine()) != null) {
                //Add all dates to priority queue
            }

            /*Remove root of PQ, then peek at new root. if new root is within the hour, increment counter. Remove new root,
        and peek at next new root. Repeat process. once new root is not within the hour of original root, we've got our
        count for that hour. Push items back on the queue.
        */
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

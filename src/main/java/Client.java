import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by jhunter on 4/5/17.
 */
public class Client {

    public static void main(String[] args) throws IOException {

        String fileString = "insight_testsuite/tests/test_features/log_input/log.txt";

        Hosts hosts = new Hosts(10);
        Resources resources = new Resources("insight_testsuite/tests/test_features/log_input/log.txt", 10);
        Hours hours = new Hours("insight_testsuite/tests/test_features/log_input/log.txt", 10);
        Blocked blocked = new Blocked("insight_testsuite/tests/test_features/log_input/log.txt");


        try (BufferedReader br = new BufferedReader(new FileReader(fileString))) {
            String line;

            while ((line = br.readLine()) != null) {
                hosts.processNextLine(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        hosts.outputResults("log_output/hosts.txt");
    }
}
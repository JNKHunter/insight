import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by jhunter on 4/5/17.
 */
public class Client {

    public static void main(String[] args) throws IOException {

        if (args.length != 5) throw new IllegalArgumentException("Incorrect number of arguments passed into program");
        String fileString = args[0];

        Hosts hosts = new Hosts(10);
        Resources resources = new Resources(10);
        Hours hours = new Hours(10);
        Blocked blocked = new Blocked();
        int linenums = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(fileString))) {
            String line;

            while ((line = br.readLine()) != null) {
                linenums += 1;
                hosts.processNextLine(line);
                resources.processNextLine(line);
                hours.processNextLine(line);
                blocked.processNextLine(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        hosts.outputResults(args[1]);
        resources.outputResults(args[2]);
        hours.outputResults(args[3]);
        blocked.outputResults(args[4]);

        System.out.println("number of lines: " + linenums);
    }
}
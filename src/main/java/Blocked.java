import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jhunter on 4/3/17.
 */
public class Blocked {

    private static String FAILED_LOGIN_CODE = "401";

    public Blocked(String fileString) {

        try (BufferedReader br = new BufferedReader(new FileReader(fileString))) {

            String logPattern = "([\\w.]+) - - \\[([/\\w\\S\\s]+) -[0-9]+] \"\\w+ [/\\w\\S]+[\\w\\S\\s]*\" ([0-9]+) [0-9\\-]+";
            Pattern p = Pattern.compile(logPattern);
            Matcher matcher;
            String line;
            Map<String, BlockedNode> failedLogins = new HashMap<>();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss");

            while ((line = br.readLine()) != null) {
                matcher = p.matcher(line);

                if (matcher.find()) {

                    if (failedLogins.containsKey(matcher.group(1))) {

                        if (matcher.group(3).equals(FAILED_LOGIN_CODE)) {
                            failedLogins.get(matcher.group(1))
                                    .logFailedAttempt(LocalDateTime.parse(matcher.group(2), formatter));
                        } else {
                            failedLogins.get(matcher.group(1)).addPossibleBlockedRequest(matcher.group(0),
                                    LocalDateTime.parse(matcher.group(2), formatter));
                        }
                    } else if (matcher.group(3).equals(FAILED_LOGIN_CODE)) {
                        failedLogins.put(matcher.group(1), new BlockedNode(matcher.group(1),
                                LocalDateTime.parse(matcher.group(2), formatter)));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Blocked blocked = new Blocked("insight_testsuite/tests/log_input/log.txt");
    }
}

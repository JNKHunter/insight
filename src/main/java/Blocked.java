import java.io.*;
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
    private static String SUCCESS_LOGIN_CODE = "401";
    private static String LOGIN_PATH = "/login";

    public Blocked(String fileString) {

        try (BufferedReader br = new BufferedReader(new FileReader(fileString))) {

            String logPattern = "([\\w.]+) - - \\[([/\\w\\S\\s]+) -[0-9]+] \"\\w+ ([/\\w\\S]+)[\\w\\S\\s]*\" ([0-9]+) [0-9\\-]+";
            Pattern p = Pattern.compile(logPattern);
            Matcher matcher;
            String line;
            Map<String, BlockedNode> failedLogins = new HashMap<>();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss");

            while ((line = br.readLine()) != null) {
                matcher = p.matcher(line);

                if (matcher.find()) {

                    if (failedLogins.containsKey(matcher.group(1))) {

                        if (matcher.group(4).equals(FAILED_LOGIN_CODE) && matcher.group(3).equals(LOGIN_PATH)) {
                            failedLogins.get(matcher.group(1)).addPossibleBlockedRequest(matcher.group(0),
                                    LocalDateTime.parse(matcher.group(2), formatter));
                            failedLogins.get(matcher.group(1))
                                    .logFailedAttempt(LocalDateTime.parse(matcher.group(2), formatter));

                        } else if (matcher.group(4).equals(SUCCESS_LOGIN_CODE) && matcher.group(3).equals(LOGIN_PATH)) {
                            if(failedLogins.get(matcher.group(1)).addPossibleBlockedRequest(matcher.group(0),
                                    LocalDateTime.parse(matcher.group(2), formatter))){

                            } else {
                                failedLogins.remove(matcher.group(1));
                            }

                        } else {
                            failedLogins.get(matcher.group(1)).addPossibleBlockedRequest(matcher.group(0),
                                    LocalDateTime.parse(matcher.group(2), formatter));
                        }
                    } else if (matcher.group(4).equals(FAILED_LOGIN_CODE) && matcher.group(3).equals(LOGIN_PATH)) {
                        failedLogins.put(matcher.group(1), new BlockedNode(matcher.group(1),
                                LocalDateTime.parse(matcher.group(2), formatter)));
                    }
                }
            }

            StringBuilder fileStringBuilder = new StringBuilder();

            failedLogins.forEach((k,v) -> {

                if(v.getBlockedRequests().size() > 0){
                    StringBuilder lineStringBuilder = new StringBuilder();
                    v.getBlockedRequests().forEach((request) -> {
                        lineStringBuilder.append(request);
                        lineStringBuilder.append("\n");
                    });
                    fileStringBuilder.insert(0, lineStringBuilder);
                }
            });

            BufferedWriter writer = new BufferedWriter(new FileWriter("log_output/blocked.txt"));
            writer.append(fileStringBuilder);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

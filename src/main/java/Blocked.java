import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Blocked {

    private static final String FAILED_LOGIN_CODE = "401";
    private static final String SUCCESS_LOGIN_CODE = "401";
    private static final String LOGIN_PATH = "/login";
    private String logPattern;
    private Pattern pattern;
    private Map<String, BlockedNode> failedLogins;
    private DateTimeFormatter formatter;
    private Matcher matcher;

    public Blocked() {
        logPattern = "([\\w.]+) - - \\[([/\\w\\S\\s]+) -[0-9]+] \"\\w+ ([/\\w\\S]+)[\\w\\S\\s]*\" ([0-9]+) [0-9\\-]+";
        pattern = Pattern.compile(logPattern);
        failedLogins = new HashMap<>();
        formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss");
    }

    public void outputResults(String outputFile) throws IOException {
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

        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        writer.append(fileStringBuilder);
        writer.close();
    }

    public void processNextLine(String line) {
        matcher = pattern.matcher(line);

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
}

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jhunter on 4/3/17.
 */
public class BlockedNode {
    public static int MAX_ATTEMPTS = 3;
    public static int ATTEMPTS_SECONDS_WINDOW = 20;
    public static int BLOCKED_MINUTES_WINDOW = 5;

    private String host;
    private List<String> blockedRequests;
    private List<LocalDateTime> loginFails;



    public BlockedNode(String host, LocalDateTime loginFailTime) {
        this.host = host;
        loginFails = new ArrayList<>();
        loginFails.add(loginFailTime);
    }

    public boolean isRequestBlocked(LocalDateTime requestTime) {
        if(loginFails.size() >= MAX_ATTEMPTS) {
            //check 3 most recent loginFails to see if they are withing 20 seconds of each other
            LocalDateTime maxAttemptsExpiry = loginFails.get(loginFails.size() - MAX_ATTEMPTS)
                    .plusSeconds(ATTEMPTS_SECONDS_WINDOW);

        }

        return false;
    }

    public void logFailedAttempt(LocalDateTime failedLoginTime) {
        loginFails.add(failedLoginTime);
    }

    public boolean addPossibleBlockedRequest(String requestString, LocalDateTime requestTime) {
        if (isBlocked()) {
            blockedRequests.add(requestString);
            return true;
        }
        return false;
    }

    public void setMostRecentLoginFail(LocalDateTime loginFail) {

    }
}

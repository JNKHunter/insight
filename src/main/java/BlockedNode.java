import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BlockedNode {
    private static final int MAX_ATTEMPTS = 3;
    private static final int ATTEMPTS_SECONDS_WINDOW = 20;
    private static final int BLOCKED_MINUTES_WINDOW = 5;

    private String host;
    private List<String> blockedRequests;
    private List<LocalDateTime> loginFails;

    public BlockedNode(String host, LocalDateTime loginFailTime) {
        this.host = host;
        loginFails = new ArrayList<>();
        blockedRequests = new ArrayList<>();
        loginFails.add(loginFailTime);
    }

    public boolean isRequestBlocked(LocalDateTime requestTime) {
        if (loginFails.size() >= MAX_ATTEMPTS) {
            // check 3 most recent loginFails to see if they are within 20 seconds of each other
            LocalDateTime maxAttemptsExpiry = loginFails.get(loginFails.size() - MAX_ATTEMPTS)
                    .plusSeconds(ATTEMPTS_SECONDS_WINDOW);

            LocalDateTime latestFailedLogin = loginFails.get(loginFails.size() - 1);

            if (maxAttemptsExpiry.isAfter(latestFailedLogin)) {
                LocalDateTime blockedWindow = latestFailedLogin.plusMinutes(BLOCKED_MINUTES_WINDOW);

                if (requestTime.isBefore(blockedWindow)) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<String> getBlockedRequests() {
        return blockedRequests;
    }

    public void logFailedAttempt(LocalDateTime failedLoginTime) {
        loginFails.add(failedLoginTime);
    }

    public boolean addPossibleBlockedRequest(String requestString, LocalDateTime requestTime) {

        if (isRequestBlocked(requestTime)) {
            blockedRequests.add(requestString);
            return true;
        }
        return false;
    }
}

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by jhunter on 4/3/17.
 */
public class BlockedNode {
    public static int MAX_ATTEMPTS;
    public static int ATTEMPTS_TIME_WINDOW;

    private String host;
    private int recentAttempts;
    private List<String> blockedRequests;
    private LocalDateTime mostRecentLoginFail;



    public BlockedNode(String host) {
        this.host = host;
    }

    public void incrementAttempts() {
        recentAttempts += 1;
    }

    public boolean isOverLimit() {
        // If the node's attempts are over the limit, return true
        return false;
    }

    public void addBlockedRequest(String requestString) {
        if (isOverLimit()) {
            blockedRequests.add(requestString);
        }
    }

    public void setMostRecentLoginFail(LocalDateTime loginFail) {

    }
}

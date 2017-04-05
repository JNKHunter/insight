import java.time.LocalDateTime;

/**
 * Created by jhunter on 4/2/17.
 */
public class HoursNode {
    private final LocalDateTime time;
    private int numEvents;

    public HoursNode(LocalDateTime time, int numEvents) {
        this.time = time;
        this.numEvents = numEvents;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public int getNumEvents() {
        return numEvents;
    }

    public void setNumEvents(int numEvents) {
        this.numEvents = numEvents;
    }
}

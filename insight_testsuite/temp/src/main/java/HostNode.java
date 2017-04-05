/**
 * Created by jhunter on 3/31/17.
 */
public class HostNode {

    private final String host;
    private int value;

    public HostNode(String host, int value) {
        this.host = host;
        this.value = value;
    }

    public void incrementValue() {
        ++value;
    }

    public String getHost() {
        return host;
    }

    public int getValue() {
        return value;
    }
}

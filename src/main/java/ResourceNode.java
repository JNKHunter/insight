/**
 * Created by jhunter on 4/1/17.
 */
public class ResourceNode {
    private final String resource;
    private long bytes;

    public ResourceNode(String resource, long bytes) {
        this.resource = resource;
        this.bytes = bytes;
    }

    public String getResource() {
        return resource;
    }

    public long getBytes() {
        return bytes;
    }

    public void incrementBytesBy(long value) {
        bytes += value;
    }
}

package joebkt;


import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class _EmoteEntry implements Serializable {

    public Map<String, String> msg = new ConcurrentHashMap();
    public long msCreated;
    public long msUpdated;
    public String createdBy;
    public String updatedBy;

    public _EmoteEntry(String emoteName) {
        this.msg.put("default", String.format("%ss.", new Object[] { emoteName}));
        this.msg.put("self",
                String.format("%ss in a mirror.", new Object[] { emoteName}));
        this.msg.put("other",
                String.format("%ss at ", new Object[] { emoteName}) + "%s.");
    }
}

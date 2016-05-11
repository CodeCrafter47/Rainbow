package joebkt;


import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class _JOT_OnlineData implements Serializable {
    public static final long serialVersionUID = -367870289683918476L;

    public Map<String, _JOT_OnlineTimeEntry> playerData = new ConcurrentHashMap();
    public long msStarted = System.currentTimeMillis();
    public Map<String, Boolean> hidePlayer = new ConcurrentHashMap();

    public _JOT_OnlineData() {}
}

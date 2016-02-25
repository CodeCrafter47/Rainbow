package joebkt;


import java.io.Serializable;


public class _JOT_OnlineTimeEntry implements Serializable {

    public long msTotal = 0L;
    public long msLastLogin = System.currentTimeMillis();
    public long msLastLogout = System.currentTimeMillis();

    public _JOT_OnlineTimeEntry() {}
}

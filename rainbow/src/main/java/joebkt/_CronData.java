package joebkt;

import java.io.Serializable;

public class _CronData implements Serializable
{
    public String jobName;
    public String cmdToRun;
    public long msDelay;
    public long msLastRun;
    
    public _CronData() {
        super();
        this.jobName = null;
        this.cmdToRun = null;
        this.msDelay = 1L;
        this.msLastRun = 0L;
    }
}

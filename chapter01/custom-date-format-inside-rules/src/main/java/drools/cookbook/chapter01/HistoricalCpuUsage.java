package drools.cookbook.chapter01;

import java.util.Date;

/**
 * 
 * @author Lucas Amador
 *
 */
public class HistoricalCpuUsage {

    private String serverName;
    private Date time;
    private int value;

    public HistoricalCpuUsage(String serverName, Date time, int value) {
        this.serverName = serverName;
        this.time = time;
        this.value = value;
    }

    public String getServerName() {
        return serverName;
    }
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }
    public Date getTime() {
        return time;
    }
    public void setTime(Date time) {
        this.time = time;
    }
    public int getValue() {
        return value;
    }
    public void setValue(int value) {
        this.value = value;
    }

}

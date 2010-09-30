package drools.cookbook.chapter01;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @author Lucas Amador
 *
 */
public class HistoricalCpuUsageQuery {

    private static final String DATE_PATTERN = "dd-MMM-yyyy HH:mm:ss";
    private static final SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);

    private String serverName;
    private Date fromDate;
    private Date toDate;
    private double maxValue;
    private double minValue;
    private double averageValue;

    public HistoricalCpuUsageQuery(String serverName, Date fromDate, Date toDate) {
        this.serverName = serverName;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    public double getMinValue() {
        return minValue;
    }

    public void setAverageValue(double averageValue) {
        this.averageValue = averageValue;
    }

    public double getAverageValue() {
        return averageValue;
    }

    @Override
    public String toString() {
        return serverName + " from: " + sdf.format(fromDate) + " to: " + sdf.format(toDate) + " minValue: " + minValue + " maxValue: " + maxValue + " averageValue: " + averageValue;
    }

}

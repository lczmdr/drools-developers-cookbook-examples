package drools.cookbook.model;

import java.util.Date;

public class FlightStatus {

    private String flight;
    private Date timestamp;
    private String origin;
    private String destination;
    private long distance;
    private float speed;
    private boolean processed;

    public String getFlight() {
        return flight;
    }

    public void setFlight(String flight) {
        this.flight = flight;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public long getDistance() {
        return distance;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public boolean getProcessed() {
        return processed;
    }
    
}

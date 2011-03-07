package drools.cookbook.model;

public class EmergencySignal {

    private String flight;
    private long distance;

    public EmergencySignal(String flight, long distance) {
        this.flight = flight;
        this.distance = distance;
    }

    public String getFlight() {
        return flight;
    }

    public void setFlight(String flight) {
        this.flight = flight;
    }

    public long getDistance() {
        return distance;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }

}

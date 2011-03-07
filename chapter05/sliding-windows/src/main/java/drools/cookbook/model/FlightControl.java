package drools.cookbook.model;

import java.util.HashMap;
import java.util.Map;

public class FlightControl {

    private String airport;
    private Map<String, FlightStatus> flights = new HashMap<String, FlightStatus>();

    public void setAirport(String airport) {
        this.airport = airport;
    }

    public String getAirport() {
        return airport;
    }

    public void setFlights(Map<String, FlightStatus> flights) {
        this.flights = flights;
    }

    public Map<String, FlightStatus> getFlights() {
        return flights;
    }

    public void addFlight(FlightStatus flightStatus) {
        flights.put(flightStatus.getFlight(), flightStatus);
    }

    public void update(FlightStatus flightStatus) {
        flights.put(flightStatus.getFlight(), flightStatus);
    }

}

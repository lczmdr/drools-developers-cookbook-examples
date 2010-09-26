package drools.cookbook.chapter01;

import java.util.ArrayList;
import java.util.List;

public class ServerAlert {

    private List<ServerEvent> events;

    public void addEvent(ServerEvent serverEvent) {
        getEvents().add(serverEvent);
    }

    public List<ServerEvent> getEvents() {
        if (events==null) {
            events = new ArrayList<ServerEvent>();
        }
        return events;
    }

    public void setEvents(List<ServerEvent> events) {
        this.events = events;
    }

}

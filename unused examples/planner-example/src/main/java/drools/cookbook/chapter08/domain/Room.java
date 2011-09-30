package drools.cookbook.chapter08.domain;

import java.util.ArrayList;
import java.util.List;

public class Room {

    public static int MAX_TALKS = 5;

    private String name;
    private boolean isMainRoom;
    private long size;
    private List<Talk> talks = new ArrayList<Talk>(MAX_TALKS);

    public Room(String name, long size, boolean isMainRoom) {
        this.name = name;
        this.size = size;
        this.isMainRoom = isMainRoom;
    }

    public Room(Room room) {
        this.name = room.name;
        this.isMainRoom = room.isMainRoom;
        this.size = room.size;
        this.talks = new ArrayList<Talk>(talks.size());
        for (Talk talk : talks) {
            talks.add(talk);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isMainRoom() {
        return isMainRoom;
    }

    public void setMainRoom(boolean isMainRoom) {
        this.isMainRoom = isMainRoom;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void addTalk(Talk talk) {
        this.talks.add(talk);
    }

    public List<Talk> getTalks() {
        return talks;
    }

    public void setTalks(List<Talk> talks) {
        this.talks = talks;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + new Boolean(isMainRoom).hashCode();
        result = (int) (prime * result + size);
        for (Talk talk : talks) {
            result = prime * result + talk.hashCode();
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Room other = (Room) obj;
        if (isMainRoom != other.isMainRoom) {
            return false;
        }
        if (size != other.size) {
            return false;
        }
        if (other.talks != null && talks != null) {
            if (!other.talks.equals(talks)) {
                return false;
            }
        }
        return true;
    }
}
